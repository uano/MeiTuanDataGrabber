package com.yzl.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzl.demo.entity.MeituanWmTurnover;
import com.yzl.demo.mapper.MeituanWmTurnoverMapper;
import com.yzl.demo.service.MeituanWmTurnoverService;
import com.yzl.demo.utils.excelTools.MyExcelExportUtil;
import com.yzl.demo.utils.meituanWMTools.WmTool;
import com.yzl.demo.vo.meituanWm.TurnoverQueryVO;
import com.yzl.demo.vo.result.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author wukang
* @description 针对表【meituan_wm_turnover】的数据库操作Service实现
* @createDate 2023-11-20 15:14:14
*/
@Service
public class MeituanWmTurnoverServiceImpl extends ServiceImpl<MeituanWmTurnoverMapper, MeituanWmTurnover>
    implements MeituanWmTurnoverService{

    @Autowired
    private MeituanWmTurnoverMapper meituanWmTurnoverMapper;

    @Override
    public Result selByCondition(TurnoverQueryVO turnoverQueryVO) {
        Page<MeituanWmTurnover> queryPage = new Page<>(turnoverQueryVO.getPageVO().getPageNum(),turnoverQueryVO.getPageVO().getPageSize());
        MeituanWmTurnover meituanWmTurnover = turnoverQueryVO.getMeituanWmTurnover();
        QueryWrapper<MeituanWmTurnover> meituanWmTurnoverQueryWrapper = new QueryWrapper<>();
        meituanWmTurnoverQueryWrapper.eq(StringUtils.isNotBlank(meituanWmTurnover.getWmTime()),"wm_time",meituanWmTurnover.getWmTime())
                .eq(StringUtils.isNotBlank(meituanWmTurnover.getWmShopId()),"wm_shop_id",meituanWmTurnover.getWmShopId());
        Page<MeituanWmTurnover> meituanWmTurnoverPage = meituanWmTurnoverMapper.selectPage(queryPage, meituanWmTurnoverQueryWrapper);
        return Result.suco(meituanWmTurnoverPage);
    }

    @Override
    public Result exportExcel(HttpServletRequest req, HttpServletResponse responseaa) {
        String wmTime = req.getParameter("wmTime");
        String wmShopId = req.getParameter("wmShopId");
        QueryWrapper<MeituanWmTurnover> meituanWmTurnoverQueryWrapper = new QueryWrapper<>();
        meituanWmTurnoverQueryWrapper.eq(StringUtils.isNotBlank(wmTime),"wm_time",wmTime)
                .eq(StringUtils.isNotBlank(wmShopId),"wm_shop_id",wmShopId);
        List<MeituanWmTurnover> meituanWmTurnovers = meituanWmTurnoverMapper.selectList(meituanWmTurnoverQueryWrapper);
        //各种转化率改为百分数
        List<MeituanWmTurnover> meituanWmTurnovers1 = WmTool.decimalToStringPer(meituanWmTurnovers);
        MyExcelExportUtil.exportExcel(meituanWmTurnovers1, MeituanWmTurnover.class,"美团外卖数据","美团外卖数据",responseaa);
        return Result.SUCESS;
    }
}




