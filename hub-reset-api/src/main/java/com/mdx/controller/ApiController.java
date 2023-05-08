package com.mdx.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.mdx.pojo.User;
import com.mdx.service.IUserService;
import com.mdx.util.PagedGridResult;
import com.mdx.util.R;
import com.mengdx.annotation.RedisLock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
