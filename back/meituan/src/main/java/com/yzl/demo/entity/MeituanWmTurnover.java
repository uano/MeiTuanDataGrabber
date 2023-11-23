package com.yzl.demo.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * @TableName meituan_wm_turnover
 */
@ExcelTarget("150")
@TableName(value ="meituan_wm_turnover")
@Data
public class MeituanWmTurnover implements Serializable {
    /**
     * 门店id
     */
    private String wmShopId;

    /**
     * 时间
     */
    @Excel(name = "时间", width = 25, height = 100)
    private String wmTime;

    /**
     * 门店名称
     */
    @Excel(name = "门店名称", width = 40, height =150)
    private String wmShopName;

    /**
     * 一周营业额
     */
    @Excel(name = "营业额", width = 15, height = 100)
    private BigDecimal wmTurnover;

    /**
     * 一周客单量
     */
    @Excel(name = "客单量", width = 15, height = 100)
    private Integer wmOrder;

    /**
     * 一周实付客单
     */
    @Excel(name = "实付客单", width = 15, height = 100)
    private BigDecimal wmCusPay;
    //一周实收客单
    @Excel(name = "实收客单", width = 15, height = 100)
    private BigDecimal wmCusIncome;

    //一周曝光量
    @Excel(name = "曝光量", width = 15, height = 100)
    private Integer wmExposure;
    //商圈前10曝光量
    @Excel(name = "商圈前10曝光量", width = 15, height = 100)
    private Integer wmExposureTopten;
    //入店转化率
    private BigDecimal wmStoreRate;
    //商圈前10入店转化率
    private BigDecimal wmStoreRateTopten;
    //下单转化率
    private BigDecimal wmOrderRate;
    //商圈前10下单转化率
    private BigDecimal wmOrderRateTopten;
    //复购率
    private BigDecimal wmRepurchaseRate;
    //商圈前10复购率
    private BigDecimal wmRepurchaseRateTopten;

    //入店转化率百分数
    @Excel(name = "入店转化率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmStoreRatePer;
    //商圈前10入店转化率百分数
    @Excel(name = "商圈前10入店转化率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmStoreRateToptenPer;

    //下单转化率百分数
    @Excel(name = "下单转化率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmOrderRatePer;

    //商圈前10下单转化率百分数
    @Excel(name = "商圈前10下单转化率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmOrderRateToptenPer;

    //复购率百分数
    @Excel(name = "复购率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmRepurchaseRatePer;

    //商圈前10复购率百分数
    @Excel(name = "商圈前10复购率", width = 15, height = 100)
    @TableField(exist = false)
    private String wmRepurchaseRateToptenPer;

    //评分
    @Excel(name = "评分", width = 15, height = 100)
    private BigDecimal wmScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}