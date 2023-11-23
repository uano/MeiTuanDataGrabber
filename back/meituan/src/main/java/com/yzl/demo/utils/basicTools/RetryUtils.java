package com.yzl.demo.utils.basicTools;

import com.yzl.demo.utils.Constants;
import com.yzl.demo.utils.requestTools.WeComHttp;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

/**
 * @ClassName RetryUtils
 * @Description TODO 重试机制方法
 * @Author wukang
 * @Date 2023/11/4 15:18
 **/
@Slf4j
public class RetryUtils {
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;
    //方法重试机制
    public static <T> T retryOnFunction(Supplier<T> function) {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                return function.get();
            } catch (Exception e) {
                retryCount++;
                log.info("函数异常，进行重试...第"+retryCount+"次");
                if (retryCount < MAX_RETRIES) {
                    sleep(RETRY_DELAY_MS);
                }
            }
        }
        log.info("重试次数已达上限，请求失败！");
        return null;
    }
    //代码块重试机制
    public static String retryOnCode(Runnable codeBlock,String agentId,String des) {
        String expDec = null;
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                codeBlock.run();
                return des+"代码块执行成功!!"; // 如果代码块顺利执行，直接返回
            } catch (Exception e) {
                e.printStackTrace();
                expDec=e.toString();
                retryCount++;
                log.info("代码块执行异常，进行重试...第"+retryCount+"次");
                if (retryCount < MAX_RETRIES) {
                    sleep(RETRY_DELAY_MS);
                }
            }
        }
        log.info("重试次数已达上限，"+des+"代码块执行失败！！");
        //进行一个消息推送
        WeComHttp.sendToPeopleText(agentId, Constants.SEND_USERS,des+"代码块执行失败！！\n异常为："+expDec);
        return "重试次数已达上限，"+des+"代码块执行失败！！";
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
