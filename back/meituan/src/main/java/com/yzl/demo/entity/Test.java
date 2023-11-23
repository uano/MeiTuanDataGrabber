package com.yzl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName test
 */
@TableName(value = "test")
@Data
public class Test implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 姓名
     */
    private String name;

    //联系方式
    private String phone;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}