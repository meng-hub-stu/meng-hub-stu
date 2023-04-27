package com.mdx.service.impl;

import com.alibaba.excel.EasyExcel;
import com.mdx.excel.UserEntityImportListener;
import com.mdx.excel.UserExport;
import com.mdx.excel.UserImport;
import com.mdx.service.IExcelService;
import com.mdx.util.R;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@Service
@AllArgsConstructor
public class ExcelServiceImpl implements IExcelService {

    @Override
    public R<String> str() {
        return R.ok("测试数据");
    }

    @Override
    public R<Object> importListener(MultipartFile file) throws IOException {
        UserEntityImportListener importListener = new UserEntityImportListener();
        EasyExcel.read(file.getInputStream(), importListener).headRowNumber(5).doReadAll();
        List<UserImport> successList = importListener.getSuccessList();
        //可以保存再redis中
        System.out.println(JSONArray.toJSONString(importListener.getList()));
        return R.ok(successList);
    }

    @Override
    public R<Object> importData(MultipartFile file) throws IOException {
        List<UserImport> list = EasyExcel.read(file.getInputStream()).headRowNumber(1).head(UserImport.class).sheet().doReadSync();
        System.out.println(JSONArray.toJSONString(list));
        return R.ok(list);
    }

    @Override
    public List<UserExport> exportData(){
        List<UserExport> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserExport userEntity = new UserExport();
            userEntity.setName("张三" + i);
            userEntity.setAge(20 + i);
            userEntity.setTime(new Date(System.currentTimeMillis() + i));
            dataList.add(userEntity);
        }
        return dataList;
    }

}
