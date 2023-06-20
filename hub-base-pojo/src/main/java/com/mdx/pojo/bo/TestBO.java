package com.mdx.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author Mengdl
 * @date 2023/06/20
 */
@ApiModel(value = "User登录对象", description = "User登录对象")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TestBO {

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "主键")
    private Long id;

}

