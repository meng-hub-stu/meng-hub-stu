package com.mdx.controller;

import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mengdl
 * @date 2023/04/27
 */
@RestController
@RequestMapping(value = "security")
@Api(value = "安全", tags = "安全")
@AllArgsConstructor
public class SecurityController {

    @GetMapping(value = "test")
    @ApiOperation(value = "测试", notes = "无参")
    public R security() {
        return R.ok("测试成功");
    }

}
