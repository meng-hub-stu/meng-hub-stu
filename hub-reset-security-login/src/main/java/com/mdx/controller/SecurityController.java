package com.mdx.controller;

import com.mdx.pojo.bo.CheckBO;
import com.mdx.pojo.group.CheckValid;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mengdl
 * @date 2023/04/27
 */
@RestController
@RequestMapping(value = "security")
@Api(value = "安全", tags = "安全")
@AllArgsConstructor
@Slf4j
public class SecurityController {

    @GetMapping(value = "test")
    @ApiOperation(value = "测试", notes = "无参")
    public R security() {
        return R.ok("测试成功");
    }

    @PostMapping(value = "add")
    @ApiOperation(value = "新增校验", notes = "对象参数")
    public R<String> add(@Validated(value = CheckValid.Add.class) @RequestBody CheckBO checkBO) {
        log.info("检验的名称{}", checkBO.getCheckName());
        log.info("检验的年龄{}", checkBO.getCheckAge());
        return R.ok("检验通过");
    }

    @PostMapping(value = "modify")
    @ApiOperation(value = "修改校验", notes = "对象参数")
    public R<String> modify(@Validated(value = CheckValid.Modify.class) @RequestBody CheckBO checkBO) {
        log.info("检验的名称{}", checkBO.getCheckName());
        log.info("检验的年龄{}", checkBO.getCheckAge());
        return R.ok("检验通过");
    }

}
