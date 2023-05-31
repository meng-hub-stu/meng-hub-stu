package com.mdx.pojo.bo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mdx.pojo.Dept;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "前端传递对象", description = "部门数据")
@EqualsAndHashCode
public class DeptBO implements Converter<DeptBO, Dept> {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门别名")
    private String deptAlias;

    @ApiModelProperty(value = "状态内容")
    private String statusName;

    @Override
    public Dept convert(DeptBO deptBO) {
        Dept dept = new Dept();
        BeanUtils.copyProperties(deptBO, dept);
        return dept;
    }

}
