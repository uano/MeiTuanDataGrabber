package com.yzl.demo.utils.meituanWMTools;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yzl.demo.entity.MeituanWm;
import com.yzl.demo.entity.MeituanWmTurnover;
import com.yzl.demo.entity.WmTimeQuery;
import com.yzl.demo.mapper.MeituanWmMapper;
import com.yzl.demo.mapper.MeituanWmTurnoverMapper;
import com.yzl.demo.mapper.WmTimeQueryMapper;
import com.yzl.demo.utils.Constants;
import com.yzl.demo.utils.basicTools.RetryUtils;
import com.yzl.demo.utils.basicTools.WeComTool;
import com.yzl.demo.utils.requestTools.OkHttpUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * @ClassName WmTool
 * @Description TODO 美团外卖工具类
 * @Author wukang
 * @Date 2023/11/22 11:51
 **/
@Component
public class WmTool {
    @Autowired
    private MeituanWmMapper meituanWmMapper;
    @Autowired
    private WmTimeQueryMapper wmTimeQueryMapper;

    @Autowired
    private MeituanWmTurnoverMapper meituanWmTurnoverMapper;
    private static WmTool wmTool;

    @PostConstruct
    public void init() {
        wmTool=this;
        wmTool.meituanWmMapper=this.meituanWmMapper;
        wmTool.wmTimeQueryMapper=this.wmTimeQueryMapper;
        wmTool.meituanWmTurnoverMapper=this.meituanWmTurnoverMapper;
    }

    //更新美团外卖最新门店信息
    public static String upWmShop(){
        //获取cookie
        String wm_cookie = WeComTool.getTokenValueByAgentId("wm_cookie");
        return  RetryUtils.retryOnCode(()->{
            //获取美团外卖最新门店
            String sync = OkHttpUtils.builder()
                    .url("https://e.waimai.meituan.com/v2/shop/businessStatus/r/poiListSearch?isOpen=0&keyWord=&cities=&pageNum=1&pageSize=500&ignoreSetRouterProxy=true")
                    .addHeader("Host", "e.waimai.meituan.com")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("sec-ch-ua", "\"Microsoft Edge\";v=\"119\", \"Chromium\";v=\"119\", \"Not?A_Brand\";v=\"24\"")
                    .addHeader("Accept", "*/*")
                    .addHeader("X-Requested-With", "XMLHttpRequest")
                    .addHeader("sec-ch-ua-mobile", "?0")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
                    .addHeader("sec-ch-ua-platform", "\"Windows\"")
                    .addHeader("Sec-Fetch-Site", "same-origin")
                    .addHeader("Sec-Fetch-Mode", "cors")
                    .addHeader("Sec-Fetch-Dest", "empty")
                    .addHeader("Referer", "https://e.waimai.meituan.com/v2/shop/manage?ignoreSetRouterProxy=true")
                    .addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                    .addHeader("Cookie",wm_cookie)
                    .get().sync();
            JSONObject jsonObject = JSONObject.fromObject(sync);
            String data = jsonObject.getString("data");
            jsonObject = JSONObject.fromObject(data);
            JSONArray dataList = jsonObject.getJSONArray("dataList");
            for (int i = 0; i < dataList.size(); i++) {
                String poiName = dataList.getJSONObject(i).getString("poiName");
                String id = dataList.getJSONObject(i).getString("id");
                //判断该门店是否存在,存在则跳过,不存在则增加该门店
                MeituanWm meituanWm = wmTool.meituanWmMapper.selectById(id);
                if(meituanWm==null){
                    //增加这个门店信息
                    MeituanWm meituanWm1 = new MeituanWm();
                    meituanWm1.setWmShopId(id);
                    meituanWm1.setWmShopName(poiName);
                    meituanWm1.setAddTime(new Date());
                    wmTool.meituanWmMapper.insert(meituanWm1);
                }
            }
        }, Constants.WM_AGENTID,"更新美团门店");
    }


    //获取美团外卖所有门店集合
    public static List<MeituanWm> getWmShop(){
        return wmTool.meituanWmMapper.selectList(null);
    }

    //时间查询表增加数据
    public static int timeQueryAdd(String timeStr){
        WmTimeQuery wmTimeQuery = new WmTimeQuery(timeStr);
        return wmTool.wmTimeQueryMapper.insert(wmTimeQuery);
    }


    //获取指定周更新的门店集合
    public static List<MeituanWmTurnover> getMeituanWmTurnover(String dateQuery){
        QueryWrapper<MeituanWmTurnover> meituanWmTurnoverQueryWrapper = new QueryWrapper<>();
        meituanWmTurnoverQueryWrapper.eq("wm_time",dateQuery);
        return wmTool.meituanWmTurnoverMapper.selectList(meituanWmTurnoverQueryWrapper);
    }


    //修改营业额表某一数据
    public static int updateMeituanWmTurnover(MeituanWmTurnover meituanWmTurnover){
        QueryWrapper<MeituanWmTurnover> meituanWmTurnoverQueryWrapper = new QueryWrapper<>();
        meituanWmTurnoverQueryWrapper.eq("wm_time",meituanWmTurnover.getWmTime()).eq("wm_shop_id",meituanWmTurnover.getWmShopId());
        return wmTool.meituanWmTurnoverMapper.update(meituanWmTurnover, meituanWmTurnoverQueryWrapper);

    }


    //查询美团外卖时间查询条件
    public static List<WmTimeQuery> timeQueryList(){
       return wmTool.wmTimeQueryMapper.selectList(null);
    }

    //查询美团外卖所有门店集合
    public static List<MeituanWm> wmShopList(){
        return wmTool.meituanWmMapper.selectList(null);
    }


    //各种转化率改为百分数
    public static List<MeituanWmTurnover> decimalToStringPer(List<MeituanWmTurnover> meituanWmTurnovers){
        for (MeituanWmTurnover meituanWmTurnover:meituanWmTurnovers) {
            meituanWmTurnover.setWmStoreRatePer(convertToPercentage(meituanWmTurnover.getWmStoreRate()));
            meituanWmTurnover.setWmStoreRateToptenPer(convertToPercentage(meituanWmTurnover.getWmStoreRateTopten()));
            meituanWmTurnover.setWmOrderRatePer(convertToPercentage(meituanWmTurnover.getWmOrderRate()));
            meituanWmTurnover.setWmOrderRateToptenPer(convertToPercentage(meituanWmTurnover.getWmOrderRateTopten()));
            meituanWmTurnover.setWmRepurchaseRatePer(convertToPercentage(meituanWmTurnover.getWmRepurchaseRate()));
            meituanWmTurnover.setWmRepurchaseRateToptenPer(convertToPercentage(meituanWmTurnover.getWmRepurchaseRateTopten()));
        }
        return meituanWmTurnovers;
    }

    //转为百分数
    public static String convertToPercentage(BigDecimal value) {
        if (value == null) {
            return "0";
        }

        BigDecimal percentage = value.multiply(BigDecimal.valueOf(100));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(percentage.setScale(2)) + "%";
    }






}
