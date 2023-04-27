package com.mdx.login.vo;

import com.mdx.pojo.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 登录成功返回对象
 * @author Mengdl
 * @date 2023/04/26
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class AuthInfo extends User {

    @ApiModelProperty(value = "token串")
    private String token;

    @ApiModelProperty(value = "过期时间")
    private Integer expiresIn;

    @ApiModelProperty(value = "刷新token")
    private String refreshToken;

    @ApiModelProperty(value = "token类型")
    private String tokenType;

}
