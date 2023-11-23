package com.yzl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName wm_time_query
 */
@TableName(value ="wm_time_query")
@Data
public class WmTimeQuery implements Serializable {
    /**
     * 时间段
     */
    @TableId
    private String wmTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public WmTimeQuery(String wmTime) {
        this.wmTime = wmTime;
    }
}