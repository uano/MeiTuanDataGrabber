package com.yzl.demo.vo.page;

import lombok.Data;

/**
 * @ClassName PageVO
 * @Description TODO 分页对象
 * @Author wukang
 * @Date 2023/10/20 13:53
 **/
@Data
public class PageVO {
    //页码
    private int pageNum;
    //每页数量
    private int pageSize;

}
