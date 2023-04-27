package com.mdx.controller;

import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import com.mdx.util.R;
import com.mengdx.annotation.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
@Api(value = "表格", tags = "表格")
public class ApiController {

    private final IUserService userService;

    @GetMapping(value = "{id}")
    @ApiOperation(value = "测试", notes = "测试")
    @RedisLock
    public R<User> test(@PathVariable("id") Long id) {
        return R.ok(userService.getById(id));
    }

}
