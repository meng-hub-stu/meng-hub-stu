package com.mdx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.mdx.pojo.Log;
import com.mdx.query.LogQuery;
import com.mdx.service.ILogService;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "log")
@Api(value = "log测试相关代码", tags = {"log测试相关的接口!"})
public class LogController {

    private final ILogService logService;

    @RequestMapping("/testLog")
    @ApiOperation(value = "测试", notes = "测试")
    public String testLog(String msg) {
        log.info("这是info信息：{}", msg);
        log.debug("这是debug信息：{}", msg);
        log.warn("这是warn信息：{}", msg);
        log.error("这是error信息：{}", msg);
        return "success";
    }

    @PostMapping
    @ApiOperation(value = "新增和修改", notes = "log对象")
    public R<String> submit(@RequestBody Log log) {
        logService.saveOrUpdate(log);
        return R.ok(log.getId().toString());
    }

    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "删除日志", notes = "日志id")
    public R<Boolean> del(@ApiParam(value = "日志id") @PathVariable(value = "id") Long id) {
        return R.ok(logService.removeById(id));
    }

    @GetMapping(value = "{id}")
    @ApiOperation(value = "日志详情", notes = "日志id")
    public R<Log> detail(@ApiParam(value = "日志id") @PathVariable(value = "id") Long id) {
        return R.ok(logService.getById(id));
    }

    @PostMapping(value = "list")
    @ApiOperation(value = "日志列表", notes = "日志对象")
    public R<IPage<Log>> detail(@RequestBody LogQuery query) {
        IPage<Log> result = logService.page(query.getPage(OrderItem.desc("create_time")), query.wrapperQuery());
        return R.ok(result);
    }

}
