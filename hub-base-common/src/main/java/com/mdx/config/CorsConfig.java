package com.mdx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 * @author Mengdl
 * @date 2021/11/08
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //1.添加cors的配置信息
        CorsConfiguration config =  new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://192.168.32.25:8080");
        config.addAllowedOrigin("http://www.mtv.com");
        config.addAllowedOrigin("http://www.mtv.com:8080");
        config.addAllowedOrigin("http://www.music.com");
        config.addAllowedOrigin("http://www.music.com:8080");
        config.addAllowedOrigin("*");
        //2.设置发送cookie消息
        config.setAllowCredentials(true);
        //3.允许请求的方式
        config.addAllowedMethod("*");
        //4.允许请求的header结构
        config.addAllowedHeader("*");
        //5为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsSource);
    }

}
