package com.mdx.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.mdx.pojo.User;
import com.mdx.pojo.bo.TestBO;
import com.mdx.service.IUserService;
import com.mdx.util.PagedGridResult;
import com.mdx.util.R;
import com.mengdx.annotation.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
@Api(value = "表格", tags = "表格")
@Slf4j
public class ApiController {

    private final IUserService userService;

    @GetMapping(value = "{id}")
    @ApiOperation(value = "测试", notes = "测试")
    @RedisLock
    public R<User> test(@PathVariable("id") Long id) {
        return R.ok(userService.getById(id));
    }

    @PostMapping(value = "test")
    @ApiOperation(value = "测试对象属性", notes = "test对象")
    public R<String> check(@RequestBody TestBO testBO) {
        log.info("测试对象->{}", JSON.toJSONString(testBO));
        return R.ok("1");
    }

    @PostMapping(value = "test01")
    @ApiOperation(value = "测试对象属性", notes = "test对象")
    public R<String> check01(TestBO testBO) {
        log.info("测试对象->{}", JSON.toJSONString(testBO));
        return R.ok("1");
    }

    @PostMapping(value = "test02")
    @ApiOperation(value = "测试对象属性", notes = "test对象")
    public R<String> check02(HttpServletRequest request) {
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        System.out.println(name  + id);
        return R.ok("1");
    }

    @GetMapping(value = "page")
    @ApiOperation(value = "测试分页", notes = "分页参数")
    public R<PagedGridResult> page (@ApiParam(name = "page", value = "页码")
                     @RequestParam(value = "page", defaultValue = "1") Integer page,
                     @ApiParam(name = "pageSize", value = "每页多少条")
                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        return R.ok(PagedGridResult.setterPagedGridResult(page, userService.list(Wrappers.<User>lambdaQuery().eq(User::getStatus, 1))));
    }

}
