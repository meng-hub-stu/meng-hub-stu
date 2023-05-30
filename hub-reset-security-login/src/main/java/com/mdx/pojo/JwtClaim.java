package com.mdx.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：YangYaNan
 * @date ：Created in 19:27 2022/5/10
 * @description ：JWT VO
 * @version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtClaim {

    /**
     * 基石系统登录账号名，唯一
     */
    private String username;

    public JwtClaim(String username, Long manufacturerId, Integer clientType) {
        this.username = username;
    }

    public static JwtClaim toJwtClaim(User user){
        return new JwtClaim(user.getUserName());
    }

}
