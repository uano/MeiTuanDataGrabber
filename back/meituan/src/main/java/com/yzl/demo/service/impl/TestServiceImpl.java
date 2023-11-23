package com.yzl.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yzl.demo.entity.Test;
import com.yzl.demo.mapper.TestMapper;
import com.yzl.demo.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author wukang
 * @description 针对表【test】的数据库操作Service实现
 * @createDate 2023-10-20 15:09:35
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
        implements TestService {

}
