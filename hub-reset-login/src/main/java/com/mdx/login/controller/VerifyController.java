package com.mdx.login.controller;

import com.alibaba.fastjson.JSON;
import com.mdx.login.util.SecureUtil;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import com.mdx.util.R;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证登录拦截等测试
 * @author Mengdl
 * @date 2023/04/26
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "ceshi")
@Slf4j
public class VerifyController {

    private final IUserService userService;

    @GetMapping(value = "{id}")
    @ApiOperation(value = "测试数据")
    public R<User> detail (@PathVariable("id") Long id) {
        log.info("登录人->{}", JSON.toJSONString(SecureUtil.getUser()));
        return R.ok(userService.getById(id));
    }

}
