package com.mdx.config.security;

import com.mdx.config.RsaKeyProperties;
import com.mdx.pojo.JwtClaim;
import com.mdx.pojo.Payload;
import com.mdx.pojo.SecurityUser;
import com.mdx.service.IUserCacheService;
import com.mdx.util.ExceptionCodeEnum;
import com.mdx.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.mdx.util.ExceptionCodeEnum.CONFLICT_TOKEN;
import static com.mdx.util.ExceptionCodeEnum.ERROR_TOKEN;
import static com.mdx.util.JwtUtils.BEARER_PREFIX;

/**
 * 检验（认证授权）
 * @author Mengdl
 * @date 2023/05/29
 */
@Slf4j
public class JwtVerifyFilter extends BasicAuthenticationFilter {

    private RsaKeyProperties prop;
    private IUserCacheService userCacheService;
    private VerifyAuthenticationEntryPoint verifyAuthenticationEntryPoint;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties prop, IUserCacheService userCacheService, VerifyAuthenticationEntryPoint verifyAuthenticationEntryPoint) {
        super(authenticationManager);
        this.prop = prop;
        this.userCacheService = userCacheService;
        this.verifyAuthenticationEntryPoint = verifyAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jtiInfo = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jti = checkJti(jtiInfo);
        String jwt = userCacheService.getJwtByJti(jti);
        checkJwt(jwt);
        Payload<JwtClaim> payload = JwtUtils.getInfoFromToken(jwt, prop.getPublicKey(), JwtClaim.class);
        //用哪个构造器都可以
        SecurityUser securityUser = new SecurityUser(userCacheService.getUserAndRenewalByUserNameAndJti(payload.getUserInfo().getUsername(), jti), jti);
        //校验权限等问题
        UsernamePasswordAuthenticationToken authResult =
                new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(), securityUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authResult);
        super.doFilterInternal(request, response, chain);
    }

    private void checkJwt(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            throw new InsufficientAuthenticationException(VerifyAuthenticationEntryPoint.VerifyConstants + ExceptionCodeEnum.EXPIRED_TOKEN.getDesc());
        }
    }

    private String checkJti(String jtiInfo) {
        if (StringUtils.isBlank(jtiInfo) || !jtiInfo.startsWith(BEARER_PREFIX)) {
            log.info("jti is blank or not Bearer, jtiInfo:{}", jtiInfo);
            throw new InsufficientAuthenticationException(VerifyAuthenticationEntryPoint.VerifyConstants + ERROR_TOKEN.getDesc());
        }
        String jti = jtiInfo.replace(BEARER_PREFIX, "");
        if (userCacheService.isBeKicked(jti)) {
            log.info("jti is expire jti:{}", jti);
            throw new InsufficientAuthenticationException(VerifyAuthenticationEntryPoint.BeKickedConstants + CONFLICT_TOKEN.getDesc());
        }
        return jti;
    }

}
