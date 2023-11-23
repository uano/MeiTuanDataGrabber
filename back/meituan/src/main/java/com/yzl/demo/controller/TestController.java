package com.yzl.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author wukang
 * @Description //TODO 控制器测试类
 * @Date 11:16 2023/11/20
 * @Param 
 * @return 
 **/
@RequestMapping("test")
@RestController
public class TestController {

    //测试请求
    @RequestMapping("/aa")
    public String aa() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();
        return "请求成功!!——>"+ipAddress;
    }


}
