package com.mdx.pojo;

/**
 * @author ：YangYaNan
 * @date ：Created in 11:36 2022/4/15
 * @description ：缓存常量类 前缀
 * @version: 1.0
 */
public class RedisConstants {

    public static final String LOGIN_USER_KEY_PREFIX = "tigerAmulet:login:user:";

    public static final String LOGIN_JTI_PREFIX = "tigerAmulet:login:jti:";

    public static final String LOGIN_JTI_EXPIRE_PREFIX = "tigerAmulet:login:jti:expire:";

    public static final String THIRD_AUTHORIZATION_PREFIX = "publicServer:third:authorization:%s";

}
