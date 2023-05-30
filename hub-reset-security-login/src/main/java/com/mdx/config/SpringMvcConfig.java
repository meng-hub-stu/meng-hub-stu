package com.mdx.config;

import com.mdx.interceptor.LogbackInterceptor;
//import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * springMvc配置类
 * @author Mengdl
 * @date 2023/04/27
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private LogbackInterceptor logbackInterceptor;
    @Resource
    private WebMvcProperties webMvcProperties;
//    @Resource
//    private WebProperties webProperties;

    /**
     * 配置swagger访问
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
//            "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/4.15.5/");
        /*registry.addResourceHandler(webMvcProperties.getStaticPathPattern())
                .addResourceLocations(webProperties.getResources().getStaticLocations());*/
        registry.addResourceHandler(webMvcProperties.getStaticPathPattern());
        super.addResourceHandlers(registry);
    }

    /**
     * json字符串转化配置
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        JacksonHttpMessageConverter jacksonHttpMessageConverter = new JacksonHttpMessageConverter();
        converters.add(new StringHttpMessageConverter());
        converters.add(jacksonHttpMessageConverter);
    }

    /**
     * 日志拦截器配置
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(logbackInterceptor).addPathPatterns("/**");
    }

    /**
     * Cors跨域请求配置
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许跨域访问的路径
        registry.addMapping("/**")
                // 允许跨域访问的域名
                .allowedOrigins("*")
//                .allowedOriginPatterns("*")
                // 允许请求方法
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                // 跨域允许时间
                .maxAge(168000)
                // 允许头部设置
                .allowedHeaders("*")
                // 是否发送cookie
                .allowCredentials(true);
    }

}
