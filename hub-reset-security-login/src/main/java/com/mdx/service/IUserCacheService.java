package com.mdx.service;

import com.mdx.pojo.User;

/**
 * @author Mengdl
 * @date 2023/05/29
 */
public interface IUserCacheService {
    /**
     * 缓存token
     * @param jti 标识
     * @param jwt token
     * @param user 用户数据
     */
    void cacheJwtAndUser(String jti, String jwt, User user);

    /**
     * 删除缓存
     * @param username 用户名
     */
    void deleteUserByUsername(String username);

    /**
     * 是否还存在jti
     * @param jti
     * @return
     */
    boolean isBeKicked(String jti);

    /**
     * 通过jti获取jwt
     * @param jti 门票
     * @return
     */
    String getJwtByJti(String jti);

    /**
     * 更新一下touken的时间
     * @param username 用户名
     * @param jti 门票
     * @return
     */
    User getUserAndRenewalByUserNameAndJti(String username, String jti);

    /**
     * 删除缓存
     * @param jti 临时票据
     * @param username 用户名
     */
    void deleteJwtAndUser(String jti, String username);

}
