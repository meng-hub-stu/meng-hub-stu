package com.mdx.config.security;

import com.mdx.util.ExceptionCodeEnum;
import com.mdx.util.R;
import com.mdx.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败，给出响应
 * @author Mengdl
 * @date 2023/05/29
 */
@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final String UserConstants = "用户";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String message;
        if(e.getMessage().contains(UserConstants)){
            ServletUtils.render(request, response, R.of(ExceptionCodeEnum.PARAM_ERROR.getCode(), e.getMessage()));
            return;
        }
        if (e instanceof BadCredentialsException) {
            message = "用户名不存在或密码错误！";
        } else if (e instanceof AccountStatusException) {
            message = "用户状态不可用！";
        } else {
            message = "登录失败！";
            log.error("[登录失败]message:{}", ExceptionUtils.getStackTrace(e));
        }
        log.info("[登录失败] - {}", message);
        ServletUtils.render(request, response, R.of(ExceptionCodeEnum.PARAM_ERROR.getCode(), message));
        return ;
    }

}
