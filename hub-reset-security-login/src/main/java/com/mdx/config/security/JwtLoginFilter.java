package com.mdx.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdx.config.RsaKeyProperties;
import com.mdx.pojo.SecurityUser;
import com.mdx.pojo.User;
import com.mdx.service.IUserCacheService;
import com.mdx.service.IUserService;
import com.mdx.util.R;
import com.mdx.util.ServletUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mdx.util.JwtUtils.BEARER_PREFIX;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * security登录
 * @author Mengdl
 * @date 2023/05/09
 */
@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private IUserCacheService userCacheService;
    private RsaKeyProperties prop;

    public JwtLoginFilter(AuthenticationManager authenticationManager, IUserCacheService userCacheService, RsaKeyProperties prop) {
        this.authenticationManager = authenticationManager;
        this.userCacheService = userCacheService;
        this.prop = prop;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[login]接收到请求");
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        log.info("[login]username:{}", user.getUserName());
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        return authenticationManager.authenticate(authRequest);
    }

    /**
     * 增加到缓存，并且响应出去
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser securityUser = ((SecurityUser) authResult.getPrincipal());
        userCacheService.cacheJwtAndUser(securityUser.getJti(), securityUser.getJwt(), securityUser.getUser());
        response.addHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + securityUser.getJti());
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        ServletUtils.render(request, response, R.ok(null));
    }

}
