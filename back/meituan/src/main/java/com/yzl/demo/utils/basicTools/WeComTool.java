package com.yzl.demo.utils.basicTools;


import com.yzl.demo.entity.Tokens;
import com.yzl.demo.mapper.TokensMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassName WeComTools
 * @Description TODO 企业微信常用工具
 * @Author wukang
 * @Date 2023/11/4 14:48
 **/
@Component
public class WeComTool {

    @Autowired
    private TokensMapper tokensMapper;

    private static WeComTool weComTool;

    @PostConstruct
    public void init() {
        weComTool=this;
        weComTool.tokensMapper=this.tokensMapper;
    }

    //获取对应自建应用当前access_token值
    public static String getTokenValueByAgentId(String agentId){
        Tokens token = weComTool.tokensMapper.selectById(agentId);
        return token.getToken();
    }
}
