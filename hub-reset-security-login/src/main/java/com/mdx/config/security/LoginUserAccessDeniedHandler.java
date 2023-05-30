package com.mdx.config.security;

import com.mdx.util.R;
import com.mdx.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户角色权限等问题
 * @author Mengdl
 * @date 2023/05/29
 */
@Slf4j
@Component
public class LoginUserAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("用户权限不足，访问[{}]失败", request.getRequestURI());
        ServletUtils.render(request, response, R.fail("权限不足！"));
    }

}
