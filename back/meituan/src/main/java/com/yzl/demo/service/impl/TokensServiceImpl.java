package com.yzl.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzl.demo.entity.Tokens;
import com.yzl.demo.service.TokensService;
import com.yzl.demo.mapper.TokensMapper;
import org.springframework.stereotype.Service;

/**
* @author wukang
* @description 针对表【tokens】的数据库操作Service实现
* @createDate 2023-11-20 10:58:56
*/
@Service
public class TokensServiceImpl extends ServiceImpl<TokensMapper, Tokens>
    implements TokensService{

}




