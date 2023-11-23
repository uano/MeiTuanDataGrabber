package com.yzl.demo.service;

import com.yzl.demo.entity.MeituanWmTurnover;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yzl.demo.vo.meituanWm.TurnoverQueryVO;
import com.yzl.demo.vo.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author wukang
* @description 针对表【meituan_wm_turnover】的数据库操作Service
* @createDate 2023-11-20 15:14:14
*/
public interface MeituanWmTurnoverService extends IService<MeituanWmTurnover> {
    //美团外卖门店字段条件查询功能
    Result selByCondition(TurnoverQueryVO turnoverQueryVO);
    //导出美团外卖excel下载功能
    Result exportExcel(HttpServletRequest requestaa, HttpServletResponse responseaa);
}
