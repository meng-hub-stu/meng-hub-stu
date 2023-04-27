package com.mdx.excel;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
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
public class UserImport extends ExcelEntity{

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date time;

}
