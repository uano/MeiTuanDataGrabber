package com.yzl.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName meituan_wm
 */
@TableName(value ="meituan_wm")
@Data
public class MeituanWm implements Serializable {
    /**
     * 外卖门店id
     */
    @TableId
    private String wmShopId;

    /**
     * 门店名称
     */
    private String wmShopName;

    /**
     * 添加时间
     */
    private Date addTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}