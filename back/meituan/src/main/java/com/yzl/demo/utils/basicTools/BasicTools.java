package com.yzl.demo.utils.basicTools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.UUID;

/**
 * @ClassName Tools
 * @Description TODO 基本工具类
 * @Author wukang
 * @Date 2023/10/20 14:03
 **/
public class BasicTools {
    //1.生成uuid主键
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    //2.将cmd curl导入postman（%--->%25）
    public static String getcURL(String curl) {
        String replace = curl.replace("%", "%25");
        return replace;
    }
    //3.base64加密方法
    public static String base64Encode(String str) {
        byte[] bytes = str.getBytes();
        byte[] encodedBytes = Base64.getEncoder().encode(bytes);
        return new String(encodedBytes);
    }
    //4.base64加密方法
    public static String base64Decode(String str) {
        byte[] encodedBytes = str.getBytes();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
        return new String(decodedBytes);
    }
    //5.String类型转为bigdecimal类型(四舍五入保留最后两位小数)
    public static BigDecimal stringToBigDecimal(String numberString) {
        BigDecimal number = new BigDecimal(numberString);
        return number.setScale(2, RoundingMode.HALF_UP);
    }
    public static BigDecimal stringToBigDecimalThree(String numberString) {
        BigDecimal number = new BigDecimal(numberString);
        return number.setScale(3, RoundingMode.HALF_UP);
    }
    //6.String类型相除，获得bigdecimal类型(四舍五入保留最后2位小数)
    public static BigDecimal divideAndRound(String dividend, String divisor) {
        BigDecimal numerator = new BigDecimal(dividend);
        BigDecimal denominator = new BigDecimal(divisor);
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

}
