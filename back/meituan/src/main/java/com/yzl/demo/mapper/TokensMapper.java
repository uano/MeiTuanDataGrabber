package com.yzl.demo.mapper;

import com.yzl.demo.entity.Tokens;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author wukang
* @description 针对表【tokens】的数据库操作Mapper
* @createDate 2023-11-20 10:58:56
* @Entity com.yzl.demo.entity.Tokens
*/
public interface TokensMapper extends BaseMapper<Tokens> {
    //修改企微对应自建应用的token值
    int updateTokenValueById(String id,String token);
}




