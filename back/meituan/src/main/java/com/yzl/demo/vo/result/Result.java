package com.yzl.demo.vo.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author wk
 * @Description TODO 统一响应对象
 * @Date 20232/10/9 16:33
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    //响应码
    private int code;
    //描述
    private String des;
    //响应数据
    private Object data;

    //响应码+描述
    public Result(int code, String des) {
        this.code = code;
        this.des = des;
    }

    public static Result suco(Object object) {
        return new Result(200, "运行成功!", object);
    }

    public static Result failStr(String string) {
        return new Result(400, string);
    }

    public static Result SUCESS = new Result(200, "运行成功！", null);
    public static Result FAIL = new Result(500, "运行失败！", null);
    public static Result NoPerm = new Result(505, "没有权限!", null);

}
