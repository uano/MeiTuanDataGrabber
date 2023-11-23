package com.yzl.demo.vo.meituanWm;

import com.yzl.demo.entity.MeituanWmTurnover;
import com.yzl.demo.vo.page.PageVO;
import lombok.Data;

/**
 * @ClassName TurnoverQueryVO
 * @Description TODO 美团外卖
 * @Author wukang
 * @Date 2023/11/18 17:11
 **/
@Data
public class TurnoverQueryVO {
    //分页条件
    private PageVO pageVO;
    //对象查询
    private MeituanWmTurnover meituanWmTurnover;
}
