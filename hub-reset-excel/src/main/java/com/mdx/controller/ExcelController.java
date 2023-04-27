package com.mdx.controller;

import com.alibaba.excel.EasyExcel;
import com.mdx.excel.UserExport;
import com.mdx.pojo.User;
import com.mdx.service.IExcelService;
import com.mdx.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@RestController
@RequestMapping(value = "/excel")
@AllArgsConstructor
@Api(value = "表格", tags = "表格")
public class ExcelController {

    private final IExcelService excelService;

    @ApiOperation(value = "导入数据")
    @PostMapping("/importData")
    public R<Object> importData(@RequestParam("file") MultipartFile file) throws IOException {
        return excelService.importData(file);
    }

    @ApiOperation(value = "模板下载和导出数据")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) throws Exception {
        List<UserExport> dataList = excelService.exportData();
        EasyExcel.write(response.getOutputStream(), UserExport.class).sheet("用户信息").doWrite(dataList);
    }
    @ApiOperation(value = "监听器导入数据")
    @PostMapping(value = "importListener")
    public R<Object> importListener(@RequestParam("file") MultipartFile file) throws Exception{
        return excelService.importListener(file);
    }

    @PostMapping(value = "user")
    @ApiOperation(value = "传入user")
    public R<User> checkUser(@Validated @RequestBody User user, BindingResult bindingResult) {
        System.out.println(user);
        Map<String, Object> map = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()){
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        System.out.println(map);
        User result = User.builder()
                .name("小明")
                .id(1L)
                .build();
        return R.ok(result);
    }

}
