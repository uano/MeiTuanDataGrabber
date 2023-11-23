package com.yzl.demo.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CrossFilter
 * @Description TODO 用作跨域
 * @Author wukang
 * @Date 2023/11/20 14:58
 **/
@WebFilter(value = "/*")
@Slf4j
public class CrossFilter implements Filter {

    @Override
    public void doFilter(ServletRequest res, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) res;
        HttpServletResponse response = (HttpServletResponse) resp;
        //跨域请求，*代表允许全部类型
        response.setHeader("Access-Control-Allow-Origin", "*");
        //允许请求方式
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //用来指定本次预检请求的有效期，单位为秒，在此期间不用发出另一条预检请求
        response.setHeader("Access-Control-Max-Age", "3600");
        //请求包含的字段内容，如有多个可用哪个逗号分隔如下
        response.setHeader("Access-Control-Allow-Headers", "AC-User-Agent,content-type,x-requested-with,Authorization, x-ui-request,lang,token,accept");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        //访问控制允许凭据，true为允许
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
        // 配置options的请求返回
        if (request.getMethod().equals("OPTIONS")) {
            response.getWriter().write("OPTIONS returns OK");
            return;
        }
        // 传递业务请求处理
        filterChain.doFilter(res, resp);
    }
}
