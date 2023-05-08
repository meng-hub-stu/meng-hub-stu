package com.mdx.pojo.bo;

import com.mdx.pojo.group.CheckValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Mengdl
 * @date 2023/05/06
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CheckBO {

    @ApiModelProperty(value = "校验名称")
    @NotBlank(groups = CheckValid.Modify.class, message = "名称不能为空")
    private String checkName;

    @ApiModelProperty(value = "检验年龄")
    @NotNull(groups = CheckValid.Add.class)
    private Integer checkAge;

}
