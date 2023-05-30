package com.mdx.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 日志添加用户名,IP地址,sessionId
 * Created by chenjianjun on 2020/1/6
 *
 */
@Component
public class LogbackInterceptor extends HandlerInterceptorAdapter {

    public static final String _IP = "ip";
    public static final String _USER_NAME = "userName";
    public static final String _SESSION_ID = "sessionId";

//    @Autowired
//    private Tracer tracer;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    private Logger logger = LoggerFactory.getLogger(LogbackInterceptor.class);

    /**
     * 拦截前方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        startTime.set(System.currentTimeMillis());
        setLogValue(request);
        return super.preHandle(request,response,handler);
    }

    /**
     * 拦截后方法
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(_IP);
        MDC.remove(_USER_NAME);
        MDC.remove(_SESSION_ID);
        if (logger.isDebugEnabled()) {
            long spentTime = System.currentTimeMillis() - startTime.get();
            if (spentTime > 500) {
                logger.debug("found slow request {}, it spent {} milliseconds.", request.getRequestURL(),spentTime);
            }
        }
    }

    private void setLogValue(HttpServletRequest request) {
        String ip = StringUtils.EMPTY;
        String sessionId = StringUtils.EMPTY;
        String userName = StringUtils.EMPTY;
        try {
            //IP地址
            ip = getIP(request);
            //用户名称
            userName = getLoginUserName();
            sessionId = request.getSession(true).getId();
        } catch (Exception ex) {
            logger.error("set log value error", ex);

        }
        MDC.put(_IP, ip);
        MDC.put(_USER_NAME, userName);
        MDC.put(_SESSION_ID, sessionId);
//        tracer.currentSpan().tag(_IP, ip);
//        tracer.currentSpan().tag(_USER_NAME, userName);
//        tracer.currentSpan().tag(_SESSION_ID, sessionId);
    }

    /**
     * 获取当前登录用户名
     * @return
     */
    public static String getLoginUserName() {
        return "anonymousUser";
    }

    /**
     * 获取客户端ip
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        if(request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return ip;
    }
}
