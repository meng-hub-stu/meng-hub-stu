package com.mdx.config.security;

import com.mdx.util.ExceptionCodeEnum;
import com.mdx.util.R;
import com.mdx.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Mengdl
 * @date 2023/05/29
 */
@Slf4j
@Component
public class VerifyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    public static final String VerifyConstants = "验证异常：";
    public static final String BeKickedConstants = "账号被顶：";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.warn("用户认证，访问[{}]失败，AuthenticationException=", request.getRequestURI(), e);
        String message = "请重新登录！";
        if(e.getMessage().contains(VerifyConstants)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ServletUtils.render(request, response, R.of(ExceptionCodeEnum.PARAM_ERROR.getCode(), e.getMessage() + message));
            return;
        }
        if(e.getMessage().contains(BeKickedConstants)){
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            ServletUtils.render(request, response, R.of(ExceptionCodeEnum.PARAM_ERROR.getCode(), e.getMessage()));
            return;
        }
        if (e instanceof InsufficientAuthenticationException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            message = "验证失败！";
            log.error("[验证失败]message:{}", ExceptionUtils.getStackTrace(e));
        }
        log.info("[验证失败] - {}", message);
        ServletUtils.render(request, response, R.of(ExceptionCodeEnum.PARAM_ERROR.getCode(), message));
    }

}
