package com.mdx.login.util;

import com.mdx.login.constant.TokenConstant;
import com.mdx.login.vo.BladeUser;
import com.mdx.util.SpringUtil;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录对象用的
 * @author Mengdl
 * @date 2023/04/26
 */
public class SecureUtil {

    public static BladeUser getUser() {
        HttpServletRequest request = SpringUtil.getCurRequest();
        if (request == null) {
            return null;
        }
        String headerToken = request.getHeader(TokenConstant.HEADER);
        String token = JwtUtil.getToken(headerToken);
        Claims claims = JwtUtil.parseJWT(token);
        if (claims == null) {
            return null;
        }
        BladeUser bladeUser = new BladeUser();
        bladeUser.setUserName((String)claims.get(TokenConstant.ACCOUNT));
        bladeUser.setName((String)claims.get(TokenConstant.USER_NAME));
        Integer id = (Integer) claims.get(TokenConstant.USER_ID);
        bladeUser.setId(Integer.toUnsignedLong(id));
        return bladeUser;
    }

}
