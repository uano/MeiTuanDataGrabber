package com.yzl.demo.dto;

import lombok.Data;

/**
 * @ClassName StampDay
 * @Description TODO 时间戳传输对象
 * @Author wukang
 * @Date 2023/11/20 17:55
 **/
@Data
public class StampDay {
    //开始时间戳
    private String startStamp;
    //结束时间戳
    private String endStamp;
}
