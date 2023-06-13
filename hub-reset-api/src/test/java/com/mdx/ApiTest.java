package com.mdx;

import com.alibaba.fastjson.JSON;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mengdl
 * @date 2023/06/13
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {

    @Autowired
    private IUserService userService;

    @Test
    public void test() {
        User user = userService.getById(1);
        log.info("查询人员的数据：{}", JSON.toJSONString(user));
    }

}
