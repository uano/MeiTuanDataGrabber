package com.yzl.demo.utils.basicTools;

import com.yzl.demo.dto.StampDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateTools
 * @Description TODO 处理时间格式相关工具类
 * @Author wukang
 * @Date 2023/11/1 16:25
 **/
public class DateTools {

    // TODO: 2023/11/1 定义时间格式============================定义时间格式============================定义时间格式============================
    public static Date date;
    public static SimpleDateFormat yyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat yyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    // TODO: 2023/11/1 时间格式转换模块=========================时间格式转换模块==============================时间格式转换模块===============================
    //1.将String转成Date类型(yyyy-MM-dd)
    public static Date stringToDateyyyyMMdd(String stringDate) throws ParseException {
        date = yyMMdd.parse(stringDate);
        return date;
    }

    //2.将String转成Date类型(yyyy-MM-dd HH:mm:ss)
    public static Date stringToDateyyyyMMddHHmmss(String stringDate) throws ParseException {
        date = yyMMddHHmmss.parse(stringDate);
        return date;
    }

    //3.将Date转化为String(yyyy-MM-dd)
    public static String dateToStringyyyyMMdd(Date date) {
        return yyMMdd.format(date);
    }

    //4.将Date转化为String(yyyy-MM-dd HH:mm:ss)
    public static String dateToStringyyyyMMddHHmmss(Date date) {
        return yyMMddHHmmss.format(date);
    }

    //5.将日期String转换成时间戳(目前日期参数是字符串)(日期格式:yyyy-MM-dd HH:mm:ss格式)
    public static String dateToStamp(String s) throws ParseException {
        Date date = yyMMddHHmmss.parse(s);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    //6.将时间戳转成日期String(目前日期参数是字符串)(日期格式:yyyy-MM-dd HH:mm:ss格式)
    public static String stampToDate(String s) {
        long lt = new Long(s);
        Date date = new Date(lt);
        return yyMMddHHmmss.format(date);
    }


    // TODO: 2023/11/1 时间计算模块===============================时间计算模块===============================时间计算模块===============================

    //1.日期类型加减天数
    public static Date computingDay(Date date, Integer days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        Date newDate = c.getTime();
        return newDate;
    }
    //2.生成Date类型当天日期起止时间戳(方法一:生成时间戳通用方法,生成的时间戳是秒级别,美团管家参数不能识别)
    public static StampDay stampOneDaySecond(Date date) throws ParseException {
        String s = dateToStringyyyyMMdd(date);
        String startTime = s + " 00:00:00";
        String endTime = s + " 23:59:59";
        String startStamp = dateToStamp(startTime);
        String endStamp = dateToStamp(endTime);
        StampDay stampDay = new StampDay();
        stampDay.setStartStamp(startStamp);
        stampDay.setEndStamp(endStamp);
        return stampDay;
    }
    //3.生成Date类型当天日期起止时间戳(方法二:生成的时间戳是毫秒级别(000->999),美团管家参数可以识别)
    public static StampDay stampOneDayMilliSecond(Date date) throws ParseException {
        String s = dateToStringyyyyMMdd(date);
        String startTime = s + " 00:00:00";
        String endTime = s + " 23:59:59";
        String startStamp = dateToStamp(startTime);
        String endStamp = dateToStamp(endTime);
        //对结束时间进行毫秒级修改,最后三位替换为999
        String endStampUse = endStamp.substring(0, endStamp.length() - 3);
        endStampUse = endStampUse + "999";
        StampDay stampDay = new StampDay();
        stampDay.setStartStamp(startStamp);
        stampDay.setEndStamp(endStampUse);
        return stampDay;
    }
    //4.自动获取前7天的起止日期(用作美团的获取门店近一周营业额的参数)
    public static String[] getPreviousWeekRange(Date currentDate) {
        String[] weekRange = new String[2];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7); // 获取前7天

        Date startDate = calendar.getTime();
        String start = dateFormat.format(startDate);

        calendar.add(Calendar.DAY_OF_YEAR, 6); // 获取前7天后的第6天
        Date endDate = calendar.getTime();
        String end = dateFormat.format(endDate);

        weekRange[0] = start;
        weekRange[1] = end;

        return weekRange;
    }





}
