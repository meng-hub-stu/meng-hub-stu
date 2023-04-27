package com.mdx.service;

import com.mdx.excel.UserExport;
import com.mdx.util.R;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
public interface IExcelService {
    /**
     * 导入数据，直接导入
     * @param file 文件
     * @exception
     * @return 返回结果
     */
    R<Object> importData(MultipartFile file) throws IOException;

    /**
     * 导出数据结果
     * @return 结果
     */
    List<UserExport> exportData();

    /**
     * 导入数据，监听器的方式
     * @param file 文件
     * @return 返回结果
     */
    R<Object> importListener(MultipartFile file) throws IOException;


    /**
     * 返回结果
     * @return 返回结果
     */
    R<String> str();
}
