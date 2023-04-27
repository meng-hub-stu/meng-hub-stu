package com.mdx.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 导出excel,也可以原始导入类
 * @author Mengdl
 * @date 2023/03/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExport extends ExcelEntity{

    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "年龄")
    private Integer age;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "操作时间")
    private Date time;

}
