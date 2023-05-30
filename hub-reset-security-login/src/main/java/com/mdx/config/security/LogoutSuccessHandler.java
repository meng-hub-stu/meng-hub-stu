package com.mdx.config.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mdx.config.RsaKeyProperties;
import com.mdx.pojo.JwtClaim;
import com.mdx.pojo.Payload;
import com.mdx.service.IUserCacheService;
import com.mdx.util.JwtUtils;
import com.mdx.util.R;
import com.mdx.util.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mdx.util.JwtUtils.BEARER_PREFIX;

/**
 * @author Mengdl
 * @date 2023/05/29
 */
@Component
@Slf4j
@AllArgsConstructor
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler{

    private final IUserCacheService userCacheService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
        } else {
            // 获取用户名
            String jti = header.replace(BEARER_PREFIX, "");
            String username = getUserNameByToken(header);
            // 删除缓存
            userCacheService.deleteJwtAndUser(jti, username);
        }
        ServletUtils.render(request, response, R.ok(null));
    }

    /**
     * 根据token获取用户名
     */
    public static String getUserNameByToken(String token) {
        if (ObjectUtil.isEmpty(token)) {return null;}

        IUserCacheService userCacheService = SpringUtil.getApplicationContext().getBean(IUserCacheService.class);
        RsaKeyProperties prop = SpringUtil.getApplicationContext().getBean(RsaKeyProperties.class);
        token = token.contains(BEARER_PREFIX) ? token.replace(BEARER_PREFIX, "") : token;
        // 获取用户名
        String jwt = userCacheService.getJwtByJti(token);
        if (ObjectUtil.isNotEmpty(jwt)) {
            Payload<JwtClaim> payload = JwtUtils.getInfoFromToken(jwt, prop.getPublicKey(), JwtClaim.class);
            return payload.getUserInfo().getUsername();
        }
        return null;
    }

}
