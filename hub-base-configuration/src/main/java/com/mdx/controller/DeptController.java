package com.mdx.controller;

import com.mdx.config.DeptLineInnerInterceptor;
import com.mdx.pojo.Dept;
import com.mdx.pojo.User;
import com.mdx.pojo.bo.DeptBO;
import com.mdx.pojo.bo.UserBO;
import com.mdx.service.IDeptService;
import com.mdx.service.IUserService;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mengdl
 * @date 2023/05/30
 */
@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping(value = "dept")
@Api(value = "配置测试", tags = "配置")
public class DeptController {

    private final IDeptService deptService;
    private final IUserService userService;

    @PostMapping(value = "add")
    @ApiOperation(value = "新增或修改", notes = "部门数据", httpMethod = "POST")
    public R<String> add(@RequestBody UserBO userBO) {
        User user = new User();
        BeanUtils.copyProperties(userBO, user);
        userService.saveOrUpdate(user);
        return R.ok(user.getId().toString());
    }

    @PostMapping(value = "submit")
    @ApiOperation(value = "新增或修改", notes = "部门数据", httpMethod = "POST")
    public R<String> submit(@RequestBody DeptBO deptBO) {
        Dept dept = deptBO.convert(deptBO);
        deptService.saveOrUpdate(dept);
        return R.ok(dept.getId().toString());
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "查看详情", notes = "部门id")
    public R<Dept> detail(@PathVariable(value = "id") Long id) {
        return R.ok(deptService.getById(id));
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "删除部门", notes = "部门id")
    public R<Boolean> del(@PathVariable(value = "id") Long id) {
        return R.ok(deptService.removeById(id));
    }

    @GetMapping(value = "list")
    @ApiOperation(value = "查看列表", notes = "无参")
    public R<List<Dept>> list() {
        DeptLineInnerInterceptor.ignoreDeptInterceptor();
        List<Dept> depts = deptService.list();
        return R.ok(depts);
    }

}
