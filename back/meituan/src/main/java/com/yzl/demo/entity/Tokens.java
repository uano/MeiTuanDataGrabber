package com.yzl.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName tokens
 */
@TableName(value ="tokens")
@Data
public class Tokens implements Serializable {
    /**
     * 自建应用agentId
     */
    @TableId
    private String id;

    /**
     * 调用凭证的值
     */
    private String token;

    /**
     * 描述
     */
    private String des;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}