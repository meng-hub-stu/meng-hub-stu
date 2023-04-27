package com.mdx.login.util;

import com.google.common.collect.Maps;
import com.mdx.login.constant.TokenConstant;
import com.mdx.login.vo.AuthInfo;
import com.mdx.login.vo.TokenInfo;
import com.mdx.pojo.User;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取token使用
 * @author Mengdl
 * @date 2023/04/26
 */
public class TokenUtil {

    /**
     * 创建认证的token
     * @param userInfo 用户数据
     * @return 返回登录成功对象
     */
    public static AuthInfo createAuthInfo(User userInfo) {
        return createAuthInfo(userInfo, 0);
    }

    public static AuthInfo createAuthInfo(User user, int appendTime) {
        Map<String, Object> param = Maps.newHashMap();
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        param.put(TokenConstant.USER_ID, user.getId());
        param.put(TokenConstant.ACCOUNT, user.getUserName());
        param.put(TokenConstant.USER_NAME, user.getName());
        TokenInfo tokenInfo = JwtUtil.createJWT(param, "audience", "issuer", TokenConstant.ACCESS_TOKEN);
        AuthInfo authInfo = new AuthInfo();
        BeanUtils.copyProperties(user, authInfo);
        authInfo.setExpiresIn(tokenInfo.getExpire());
        authInfo.setToken(tokenInfo.getToken());
        authInfo.setRefreshToken(createRefreshToken(user).getToken());
        authInfo.setTokenType(TokenConstant.BEARER);
        return authInfo;
    }
    /**
     * 创建refreshToken
     * @param user 用户信息
     * @return refreshToken
     */
    private static TokenInfo createRefreshToken(User user) {
        Map<String, Object> param = new HashMap<>(16);
        param.put(TokenConstant.USER_ID, user.getId());
        param.put(TokenConstant.ACCOUNT, user.getUserName());
        param.put(TokenConstant.USER_NAME, user.getName());
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN);
        return JwtUtil.createJWT(param, "audience", "issuer", TokenConstant.REFRESH_TOKEN);
    }

}
