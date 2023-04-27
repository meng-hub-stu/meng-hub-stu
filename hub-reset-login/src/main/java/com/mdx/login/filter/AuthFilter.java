package com.mdx.login.filter;

import com.mdx.login.constant.TokenConstant;
import com.mdx.login.util.JwtUtil;
import com.mdx.util.JsonUtils;
import com.mdx.util.R;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mdx.login.constant.TokenConstant.REDIS_USER_TOKEN;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 实现简单的登录
 * @author Mengdl
 * @date 2023/04/26
 */
@Slf4j
@Component
@AllArgsConstructor
public class AuthFilter implements Filter {

    private static List<String> skipUrl = new ArrayList<>();
    private final RedisTemplate<String, Object> redisTemplate;

    static {
        skipUrl.add("/login");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        log.info("进入拦截器");
        //获取请求路径, 进行放过
        String path = req.getRequestURL().toString();
        if(isSkip(path)){
            filterChain.doFilter(req, resp);
            return ;
        }
        String headerToken = req.getHeader(TokenConstant.HEADER);
        String paramToken = req.getParameter(TokenConstant.HEADER);
        if (isBlank(headerToken) && isBlank(paramToken)) {
            returnErrorResult(resp, R.errorMap("token不能为空"));
            return ;
        }
        String token = JwtUtil.getToken(headerToken);
        Claims claims = JwtUtil.parseJWT(token);
        if (claims == null) {
            returnErrorResult(resp, R.errorMap("token不正确"));
            return ;
        }
        //可以使用redis和cookie实现单点登录
        String redisToken = (String)redisTemplate.opsForValue().get(REDIS_USER_TOKEN + ":" + claims.get(TokenConstant.USER_ID));
        if (!token.equals(redisToken)) {
            returnErrorResult(resp, R.errorMap("已在别处登录"));
            return ;
        }
        log.info("登录人->{}", claims.get(TokenConstant.USER_NAME));
        filterChain.doFilter(req, resp);
    }

    /**
     * 错误响应
     * @param response 响应体
     * @param result 响应内容
     */
    public void returnErrorResult(HttpServletResponse response,
                                  R result){
        ServletOutputStream outputStream = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            outputStream = response.getOutputStream();
            outputStream.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSkip(String path) {
        return skipUrl.stream().anyMatch(pattern -> matcher(pattern, path));
    }

    private static boolean matcher(String pattern, String path) {
        return new AntPathMatcher().match("**" + pattern, path);
    }

}
