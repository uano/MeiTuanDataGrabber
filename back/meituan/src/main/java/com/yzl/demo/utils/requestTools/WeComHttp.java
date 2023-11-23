package com.yzl.demo.utils.requestTools;


import com.yzl.demo.mapper.TokensMapper;
import com.yzl.demo.utils.basicTools.RetryUtils;
import com.yzl.demo.utils.basicTools.WeComTool;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName WeComHttp
 * @Description TODO 与企业微信进行消息交互
 * @Author wukang
 * @Date 2023/11/22 16:27
 **/
@Slf4j
@Component
public class WeComHttp {

    @Autowired
    private TokensMapper tokensMapper;
    
    private static WeComHttp weComHttp;

    @PostConstruct
    public void init() {
        weComHttp=this;
        weComHttp.tokensMapper=this.tokensMapper;
    }
    //1.生成某一企业微信自建应用access_token   corpid:企业编码  corpsecret:自建应用Secret
    public static String getWeComAccessToken(String corpid,String corpsecret,String agentId){
        return RetryUtils.retryOnCode(()->{
            String sync = OkHttpUtils.builder()
                        .url("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + corpsecret)
                        .get()
                        .sync();
                //获取access_token并替换
                JSONObject jsonObject = JSONObject.fromObject(sync);
                String access_token = jsonObject.getString("access_token");
                weComHttp.tokensMapper.updateTokenValueById(agentId,access_token);
        },agentId,"更换美团外卖token");
    }
    //2.企微自建应用给企微对应人员发送消息->文本信息
    public static String sendToPeopleText(String agentId,String users,String text){
        //获取agentId当前access_token
        String tokenValue = WeComTool.getTokenValueByAgentId(agentId);
        return RetryUtils.retryOnCode(()->{
            //发送消息
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n   \"touser\" : \""+users+"\",\r\n   \"toparty\" : \"PartyID1|PartyID2\",\r\n   \"totag\" : \"TagID1 | TagID2\",\r\n   \"msgtype\" : \"text\",\r\n   \"agentid\" : "+agentId+",\r\n   \"text\" : {\r\n       \"content\" : \""+text+"\"\r\n   },\r\n   \"safe\":0,\r\n   \"enable_id_trans\": 0,\r\n   \"enable_duplicate_check\": 0,\r\n   \"duplicate_check_interval\": 1800\r\n}");
            String sync = OkHttpUtils.builder()
                    .url("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+tokenValue)
                    .addHeader("Content-Type", "application/json")
                    .postJsonBody(body)
                    .sync();
            System.out.println(sync);
        },agentId,agentId+"发送消息");
    }

    //3.给对应的群发送消息
}
