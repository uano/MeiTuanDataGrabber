package com.yzl.demo.controller.meituanWM;

import com.yzl.demo.entity.MeituanWm;
import com.yzl.demo.entity.MeituanWmTurnover;
import com.yzl.demo.entity.WmTimeQuery;
import com.yzl.demo.service.MeituanWmTurnoverService;
import com.yzl.demo.utils.Constants;
import com.yzl.demo.utils.basicTools.BasicTools;
import com.yzl.demo.utils.basicTools.DateTools;
import com.yzl.demo.utils.basicTools.RetryUtils;
import com.yzl.demo.utils.basicTools.WeComTool;
import com.yzl.demo.utils.meituanWMTools.WmTool;
import com.yzl.demo.utils.requestTools.OkHttpUtils;
import com.yzl.demo.utils.requestTools.WeComHttp;
import com.yzl.demo.vo.meituanWm.TurnoverQueryVO;
import com.yzl.demo.vo.result.Result;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName WmSpider
 * @Description TODO 美团外卖抓包对应字段需求
 * @Author wukang
 * @Date 2023/11/20 10:50
 **/
@RestController
@RequestMapping("meituan")
public class WmSpider {


    @Autowired
    private MeituanWmTurnoverService meituanWmTurnoverService;

    //定时(2h换一次)更换企业微信access_token
    @Scheduled(cron = "0 0 */2 * * ?")
    @RequestMapping("/upWeComAccessToken")
    public String upWeComAccessToken(){
        return WeComHttp.getWeComAccessToken(Constants.CORPID,Constants.WM_CORPSECRET,Constants.WM_AGENTID);
    }

    //每天检查美团外卖门店是否增加,1.更新门店 2.当cookie过期了,人工手动替换
    @Scheduled(cron = "0 0 10 * * ?")
    @RequestMapping("/upWmShop")
    @Transactional
    public String upWmShop(){
        return WmTool.upWmShop();
    }

    // TODO: 2023/11/14 调用接口模块=============================调用接口模块============================= 调用接口模块=============================

    //每周一更新所有门店营业额数据相关接口,
    @Transactional
    @Scheduled(cron = "0 0 7 ? * MON")
    @RequestMapping("/turnover")
    public String turnover() throws InterruptedException {
        //获取近一周时间参数
        String[] week = DateTools.getPreviousWeekRange(new Date());
        //表中时间查询条件
        String queryTime=week[0] + "-" + week[1];
        //获取acctId凭证
        String acctId = WeComTool.getTokenValueByAgentId("acctId");
        //获取所有门店
        List<MeituanWm> wmShop = WmTool.getWmShop();
        //一周新增的门店集合
        ArrayList<MeituanWmTurnover> meituanWmTurnovers = new ArrayList<>();
        //获取每个门店
        for (MeituanWm meituanWm : wmShop) {
            String wmShopId = meituanWm.getWmShopId();
            String wmShopName = meituanWm.getWmShopName();
            RetryUtils.retryOnCode(() -> {
                String sync = OkHttpUtils.builder()
                        .url("https://waimaieapp.meituan.com/bizdata/overviewV4/history?token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI*&wmPoiId="+wmShopId+"&acctId=" + acctId + "&appType=3&beginDate=" + week[0] + "&endDate=" + week[1] + "&durationType=2&terminalType=2")
                        .addHeader("Host", "waimaieapp.meituan.com")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("sec-ch-ua", "\"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("Sec-Fetch-Site", "same-origin")
                        .addHeader("Sec-Fetch-Mode", "cors")
                        .addHeader("Sec-Fetch-Dest", "empty")
                        .addHeader("Referer", "")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                        .addHeader("Cookie", "")
                        .get().sync();
//                System.out.println(sync);
                JSONObject jsonObject = JSONObject.fromObject(sync);
                String data = jsonObject.getString("data");
                jsonObject = JSONObject.fromObject(data);
                String business = jsonObject.getString("business");
                jsonObject = JSONObject.fromObject(business);
                //获取本周营业额
                String settleAmount = jsonObject.getString("settleAmount");
                //获取本周单量
                String orderCnt = jsonObject.getString("orderCnt");
                //获取本周实付客单并转为bigdecimal类型,四舍五入保留最后两位小数
                String avgPriceTest = jsonObject.getString("avgPrice");
                BigDecimal avgPrice = BasicTools.stringToBigDecimal(avgPriceTest);
                //计算本周实收客单
                BigDecimal bigDecimal = BasicTools.divideAndRound(settleAmount, orderCnt);

//                System.out.println(wmShopName+"：本周营业额:" + settleAmount+"，本周单量:" + orderCnt+"，本周实付客单价:" + avgPrice+"，本周实收客单价:" + bigDecimal);
                MeituanWmTurnover meituanWmTurnover = new MeituanWmTurnover();
                meituanWmTurnover.setWmTurnover(new BigDecimal(settleAmount));
                meituanWmTurnover.setWmCusPay(avgPrice);
                meituanWmTurnover.setWmCusIncome(bigDecimal);
                meituanWmTurnover.setWmShopId(wmShopId);
                meituanWmTurnover.setWmShopName(wmShopName);
                meituanWmTurnover.setWmOrder(Integer.valueOf(orderCnt));
                meituanWmTurnover.setWmTime(queryTime);
                meituanWmTurnovers.add(meituanWmTurnover);
            }, Constants.WM_AGENTID, "获取美团外卖"+wmShopName+"近一周营业额相关数据");

            Thread.sleep(500); // 延迟500毫秒
        }
        //将数据添加至营业额表哥
        meituanWmTurnoverService.saveBatch(meituanWmTurnovers);
        //将时间查询条件放入美团外卖时间查询表
        WmTool.timeQueryAdd(queryTime);
        return "美团外卖" + week[0] + "至" + week[1] + "营业额更新成功!";

    }

    //每周一更新所有门店曝光率,转化率相关字段的呢
    @Transactional
    @Scheduled(cron = "0 30 7 ? * MON")
    @RequestMapping("/rate")
    public String rate() throws InterruptedException {
        //获取近一周时间参数
        String[] week = DateTools.getPreviousWeekRange(new Date());
        String queryTime=week[0] + "-" + week[1];
        //获取acctId凭证
        String acctId = WeComTool.getTokenValueByAgentId("acctId");
        //获取第一次更新的本周门店营业额集合
        List<MeituanWmTurnover> meituanWmTurnovers = WmTool.getMeituanWmTurnover(queryTime);
        if(meituanWmTurnovers.isEmpty()){
            WeComHttp.sendToPeopleText(Constants.WM_AGENTID,Constants.SEND_USERS,"营业相关更新失败导致转化率等更新失败!!");
        }
        for (MeituanWmTurnover meituanWmTurnover: meituanWmTurnovers) {
            String wmShopId = meituanWmTurnover.getWmShopId();
            String wmShopName = meituanWmTurnover.getWmShopName();
            RetryUtils.retryOnCode(()->{
                String sync = OkHttpUtils.builder()
                        .url("https://waimaieapp.meituan.com/gw/bizdata/flow/single/r/overview?acctId="+acctId+"&wmPoiId="+wmShopId+"&durationType=2&tabType=3&bizLineType=0&type=1&ignoreSetRouterProxy=true")
                        .addHeader("Host", "waimaieapp.meituan.com")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("sec-ch-ua", "\"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("Sec-Fetch-Site", "same-origin")
                        .addHeader("Sec-Fetch-Mode", "cors")
                        .addHeader("Sec-Fetch-Dest", "empty")
                        .addHeader("Referer", "")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                        .addHeader("Cookie", "")
                        .get().sync();

//        System.out.println(sync);
                JSONObject jsonObject = JSONObject.fromObject(sync);
                //获取对应字段的值
                String data = jsonObject.getString("data");
                jsonObject = JSONObject.fromObject(data);
                JSONArray list = jsonObject.getJSONArray("list");
                //曝光人数
                String exposure= list.getJSONObject(0).getString("base");
                //商圈前十曝光人数
                String exposureTen= list.getJSONObject(10).getString("base");
                //入店转化率
                String storeRate = list.getJSONObject(3).getString("base");
                //商圈前十入店转化率
                String storeRateTen = list.getJSONObject(13).getString("base");
                //下单转化率
                String orderRate = list.getJSONObject(4).getString("base");
                //商圈前十下单转换率
                String orderRateTen = list.getJSONObject(14).getString("base");
                System.out.println(wmShopName+",曝光人数："+exposure+",商圈前10曝光人数:"+exposureTen+",入店转化率："+storeRate+",商圈前10入店转化率："+storeRateTen+",下单转化率："+orderRate+",商圈前十下单转化率："+orderRateTen);
                //设置对应的值
                if(!(exposure.equals("null"))){
                    meituanWmTurnover.setWmExposure(Integer.parseInt(exposure));
                }
                if(!(exposureTen.equals("null"))){
                    meituanWmTurnover.setWmExposureTopten(Integer.parseInt(exposureTen));
                }
                if(!(storeRate.equals("null"))){
                    meituanWmTurnover.setWmStoreRate(new BigDecimal(storeRate));
                }
                if(!(storeRateTen.equals("null"))){
                    meituanWmTurnover.setWmStoreRateTopten(new BigDecimal(storeRateTen));
                }
                if(!(orderRate.equals("null"))){
                    meituanWmTurnover.setWmOrderRate(new BigDecimal(orderRate));
                }
                if(!(orderRateTen.equals("null"))){
                    meituanWmTurnover.setWmOrderRateTopten(new BigDecimal(orderRateTen));
                }
                WmTool.updateMeituanWmTurnover(meituanWmTurnover);
            },Constants.WM_AGENTID,"获取美团外卖"+wmShopName+"近一周曝光数及相关转化率");
            Thread.sleep(500); // 延迟500毫秒
        }
        return "美团外卖" + week[0] + "至" + week[1] + "曝光人数及转化率模块更新成功!";
    }

    //每周一更新所有门店的复购率及商圈前10复购率
    @Transactional
    @Scheduled(cron = "0 40 7 ? * MON")
    @RequestMapping("/repurchaseRate")
    public String repurchaseRate() throws InterruptedException {
        //获取近一周时间参数
        String[] week = DateTools.getPreviousWeekRange(new Date());
        String queryTime=week[0] + "-" + week[1];
        List<MeituanWmTurnover> meituanWmTurnovers = WmTool.getMeituanWmTurnover(queryTime);
        if(meituanWmTurnovers.isEmpty()){
            WeComHttp.sendToPeopleText(Constants.WM_AGENTID,Constants.SEND_USERS,"营业相关更新失败导致复购率等更新失败!!");
        }
        for (MeituanWmTurnover meituanWmTurnover:meituanWmTurnovers) {
            String wmShopId = meituanWmTurnover.getWmShopId();
            String wmShopName = meituanWmTurnover.getWmShopName();
            RetryUtils.retryOnCode(()->{
                String sync = OkHttpUtils.builder()
                        .url("https://waimaieapp.meituan.com/bizdata/single/customer/overview?token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI*&wmPoiId="+wmShopId+"&acctId=134135563&appType=3&customerType=1&durationType=2&circleType=0&yodaReady=h5&csecplatform=4&csecversion=2.3.1&mtgsig=%25%257B%25%2522a1%25%2522%25%253A%25%25221.1%25%2522%25%252C%25%2522a2%25%2522%25%253A1700287878950%25%252C%25%2522a3%25%2522%25%253A%25%2522zv9zwz18ww245615069460368w7w505881yxy4600869795887v8823v%25%2522%25%252C%25%2522a5%25%2522%25%253A%25%2522vF%25%252FGFXPuYEcs66LzHKKdkI%25%253D%25%253D%25%2522%25%252C%25%2522a6%25%2522%25%253A%25%2522hs1.4aOG4x69iuIGtADfqn9IKcVxxyp%25%252BgYsKiSkoO1MSjuvobcTgAcgN9yo7w%25%252FsnTxcdhTsQw0FfmGOEby4XUIDeK2g%25%253D%25%253D%25%2522%25%252C%25%2522x0%25%2522%25%253A4%25%252C%25%2522d1%25%2522%25%253A%25%2522d20597ecb36fac25390ec21f616a3bda%25%2522%25%257D")
                        .addHeader("Host", "waimaieapp.meituan.com")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("sec-ch-ua", "\"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"")
                        .addHeader("Accept", "application/json, text/plain, */*")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("Sec-Fetch-Site", "same-origin")
                        .addHeader("Sec-Fetch-Mode", "cors")
                        .addHeader("Sec-Fetch-Dest", "empty")
                        .addHeader("Referer", "")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                        .addHeader("Cookie", "")
                        .get().sync();
                JSONObject jsonObject = JSONObject.fromObject(sync);
                JSONArray data = jsonObject.getJSONArray("data");
                //复购率
                String base = data.getJSONObject(2).getString("base");
                //商圈前10复购率
                String compare = data.getJSONObject(2).getString("compare");
                System.out.println(wmShopName+":复购率:"+base+",商圈前10复购率:"+compare);
                //非空计算本周商圈复购率
                if(!("null".equals(base))){
                    BigDecimal bigDecimal = BasicTools.stringToBigDecimalThree(base);
                    meituanWmTurnover.setWmRepurchaseRate(bigDecimal);
                }
                if(!("null".equals(base))&&!("null".equals(compare))){
                    BigDecimal baseDecimal = BasicTools.stringToBigDecimalThree(base);
                    BigDecimal compareDecimal = BasicTools.stringToBigDecimalThree(compare);
                    BigDecimal finalCompare= baseDecimal.subtract(compareDecimal);
                    meituanWmTurnover.setWmRepurchaseRateTopten(finalCompare);
                }
                WmTool.updateMeituanWmTurnover(meituanWmTurnover);
            }, Constants.WM_AGENTID,"获取美团外卖"+wmShopName+"近一周复购率");
            Thread.sleep(500); // 延迟500毫秒
        }
        return "美团外卖" +queryTime+ "复购率模块更新成功!";
    }

    //每周一更新所有门店的本周的评分
    @Transactional
    @Scheduled(cron = "0 50 7 ? * MON")
    @RequestMapping("/score")
    public String score() throws InterruptedException {
        //获取近一周时间参数
        String[] week = DateTools.getPreviousWeekRange(new Date());
        String queryTime=week[0] + "-" + week[1];
        List<MeituanWmTurnover> meituanWmTurnovers = WmTool.getMeituanWmTurnover(queryTime);
        if(meituanWmTurnovers.isEmpty()){
            WeComHttp.sendToPeopleText(Constants.WM_AGENTID,Constants.SEND_USERS,"营业相关更新失败导致门店评分等更新失败!!");
        }
        for (MeituanWmTurnover meituanWmTurnover:meituanWmTurnovers){
            String wmShopId = meituanWmTurnover.getWmShopId();
            String wmShopName = meituanWmTurnover.getWmShopName();
            RetryUtils.retryOnCode(()->{
                String sync = OkHttpUtils.builder()
                        .url("https://waimaieapp.meituan.com/gw/customer/comment/scores?ignoreSetRouterProxy=true&acctId=134135563&wmPoiId="+wmShopId+"&token=06cAdJJG3nRwUFV26e5-5fk9ro0hjZ6EXxzXPi4KoyvI%25%252A&appType=3&source=0")
                        .addHeader("Host", "waimaieapp.meituan.com")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("sec-ch-ua", "\"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"")
                        .addHeader("sec-ch-ua-mobile", "?0")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36 Edg/119.0.0.0")
                        .addHeader("sec-ch-ua-platform", "\"Windows\"")
                        .addHeader("Accept", "*/*")
                        .addHeader("Sec-Fetch-Site", "same-origin")
                        .addHeader("Sec-Fetch-Mode", "cors")
                        .addHeader("Sec-Fetch-Dest", "empty")
                        .addHeader("Referer", "")
                        .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                        .addHeader("Cookie", "")
                        .get().sync();
                JSONObject jsonObject = JSONObject.fromObject(sync);
                String data = jsonObject.getString("data");
                jsonObject = JSONObject.fromObject(data);
                String poiScore = jsonObject.getString("poiScore");
//                System.out.println(wmShopName+"外卖评分为:"+poiScore);
                meituanWmTurnover.setWmScore(new BigDecimal(poiScore));
                WmTool.updateMeituanWmTurnover(meituanWmTurnover);
            },Constants.WM_AGENTID,"获取美团外卖"+wmShopName+"近一周评分");
            Thread.sleep(500);
        }
        return "美团外卖" +queryTime+ "评分模块更新成功!";
    }

    // TODO: 2023/11/20 查询功能

    //查询时间段条件功能
    @RequestMapping("/queryTimeList")
    public List<WmTimeQuery> queryTimeList(){
        return WmTool.timeQueryList();
    }

    //查询门店集合功能
    @RequestMapping("/wmShopList")
    public List<MeituanWm> wmShopList(){
        return WmTool.wmShopList();
    }

    //查询周营业额字段功能
    @RequestMapping("/select")
    public Result select(@RequestBody TurnoverQueryVO turnoverQueryVO){
        return meituanWmTurnoverService.selByCondition(turnoverQueryVO);
    }

    //导出美团外卖excel功能
    @RequestMapping("/exportExcel")
    public Result exportExcel(HttpServletRequest req, HttpServletResponse responseaa){
        return meituanWmTurnoverService.exportExcel(req,responseaa);
    }

}
