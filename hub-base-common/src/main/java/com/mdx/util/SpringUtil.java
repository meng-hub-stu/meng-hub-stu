package com.mdx.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * spring启动常用
 * @author Mengdl
 * @date 2023/04/27
 */
@Component
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getCurRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getCurResponse()
    {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 刷新spring的配置。供统一配置更新的时候使用
     */
    public static void refresh()
    {
        XmlWebApplicationContext context =
                (XmlWebApplicationContext) applicationContext;
        context.refresh();
    }

    /**
     * 是否存在bean
     * @param name
     * @return
     */
    public static boolean containsBean(String name){
        return applicationContext.containsBean(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) applicationContext.getBean(name);
    }

    /**
     * @return 获取当前环境
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }

    /**
     * @return 判断是否开发环境
     */
    public static boolean isDevActive()
    {
        String[] profiles= SpringUtil.getApplicationContext().getEnvironment().getActiveProfiles();
        return Arrays.asList(profiles).stream().anyMatch(k->k.endsWith("-dev"));
    }


    public static String getEvnProfile() {
        return System.getProperty("spring.profiles.active");
    }

    public static boolean isProductionEnv(){
        return "prod".equals(getEvnProfile());
    }

    public static boolean isTestEnv() {
        return "test".equals(getEvnProfile());
    }

    public static boolean isQaEnv() {
        return "qa".equals(getEvnProfile());
    }

    public static String getPCDBSchema() {
        if(isProductionEnv()) {
            return "bedrock_pc";
        } else if(isQaEnv()){
            return "pc_k8s";
        } else {
            return "h_pc";
        }
    }

}
