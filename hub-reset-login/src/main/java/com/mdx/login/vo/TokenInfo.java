package com.mdx.login.vo;

import lombok.Data;

/**
 * jwt生成token对象参数
 * @author Mengdl
 * @date 2023/04/26
 */
@Data
public class TokenInfo {
    /**
     * 令牌值
     */
    private String token;

    /**
     * 过期秒数
     */
    private int expire;

}
