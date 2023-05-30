package com.mdx.service.impl;

import com.mdx.pojo.RedisConstants;
import com.mdx.pojo.User;
import com.mdx.service.IUserCacheService;
import com.mdx.util.CacheUtil;
import com.mdx.util.GsonSerializeUtils;
import com.mdx.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Mengdl
 * @date 2023/05/29
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserCacheServiceImpl implements IUserCacheService {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisUtils redisUtils;

    @Override
    public void deleteJwtAndUser(String jti, String username) {
        if (StringUtils.isNotEmpty(username)) {
            CacheUtil.deleteKey(stringRedisTemplate, getUserKey(jti, username));
        }
        CacheUtil.deleteKey(stringRedisTemplate, getJwtKey(jti));
    }

    @Override
    public User getUserAndRenewalByUserNameAndJti(String username, String jti) {
        String userJson = CacheUtil.getValue(stringRedisTemplate, getUserKey(jti, username));
        CacheUtil.addExpire(stringRedisTemplate, getUserKey(jti, username), 2, TimeUnit.HOURS);
        CacheUtil.addExpire(stringRedisTemplate, getJwtKey(jti), 1, TimeUnit.HOURS);
        return GsonSerializeUtils.toBean(userJson, User.class);
    }

    @Override
    public String getJwtByJti(String jti) {
        return CacheUtil.getValue(stringRedisTemplate, getJwtKey(jti));
    }

    @Override
    public void cacheJwtAndUser(String jti, String jwt, User user) {
        String username = user.getUserName();
        if(!User.MULTIPLE_SIMULTANEOUS_ONLINE_USERS.contains(username)){
            Set<String> userKeys = stringRedisTemplate.keys(getUserKey(null, username));
            for (String userKey : userKeys) {
                Long expireTime = stringRedisTemplate.getExpire(userKey);
                CacheUtil.deleteKey(stringRedisTemplate, getJtiKeyByUserKey(userKey));
                CacheUtil.putValueWithExpire(stringRedisTemplate, getJtiExpireKeyByUserKey(userKey), "", expireTime.intValue(), TimeUnit.SECONDS);
            }
            deleteUserByUsername(username);
        }
        CacheUtil.putValueWithExpire(stringRedisTemplate, getUserKey(jti, username), GsonSerializeUtils.toString(user), 2, TimeUnit.HOURS);
        CacheUtil.putValueWithExpire(stringRedisTemplate, getJwtKey(jti), jwt, 1, TimeUnit.HOURS);
    }

    @Override
    public void deleteUserByUsername(String username) {
        Set<String> onLineKeys = stringRedisTemplate.keys(getUserKey(null, username));
        for (String onLineKey : onLineKeys) {
            CacheUtil.deleteKey(stringRedisTemplate, onLineKey);
        }
    }

    @Override
    public boolean isBeKicked(String jti) {
        return CacheUtil.hasKey(stringRedisTemplate, getJtiExpireKeyByJti(jti));
    }

    private String getJwtKey(String jti) {
        return RedisConstants.LOGIN_JTI_PREFIX + jti;
    }

    private String getUserKey(String jti, String username) {
        String userKey;
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(jti)) {
            userKey = RedisConstants.LOGIN_USER_KEY_PREFIX + username + ":" + jti;
        } else if (StringUtils.isEmpty(username) && StringUtils.isEmpty(jti)) {
            userKey = RedisConstants.LOGIN_USER_KEY_PREFIX + "*";
        } else {
            userKey = RedisConstants.LOGIN_USER_KEY_PREFIX + username + ":" + "*";
        }
        return userKey;
    }

    private String getJtiExpireKeyByJti(String jti) {
        return RedisConstants.LOGIN_JTI_EXPIRE_PREFIX + jti;
    }

    private String getJtiKeyByUserKey(String userKey) {
        return RedisConstants.LOGIN_JTI_PREFIX + userKey.split(":")[4];
    }

    private String getJtiExpireKeyByUserKey(String userKey) {
        return RedisConstants.LOGIN_JTI_EXPIRE_PREFIX + userKey.split(":")[4];
    }

}
