package com.mdx.config;

import com.mdx.config.security.*;
import com.mdx.service.IUserCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2023/05/09
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityUserDetailsService securityUserDetailsService;
    private final LoginFailureHandler loginFailureHandler;
    private final RsaKeyProperties prop;
    private final IUserCacheService userCacheService;
    private final LoginUserAccessDeniedHandler accessDeniedHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final VerifyAuthenticationEntryPoint verifyAuthenticationEntryPoint;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(permitUrls);
        web.ignoring().antMatchers(HttpMethod.GET, "/news","/news/{\\d+}","/resources/images/news/**");
    }

    /**
     * 认证授权控制器 （管理者 提供者 认证者 授权者）
     *
     * @param auth
     * @return void
     * @Author YangYaNan
     * @Date Created in 10:31 2022/4/6
     * @Description
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter(super.authenticationManager(), userCacheService, prop);
        jwtLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().addFilter(jwtLoginFilter)
                .addFilter(new JwtVerifyFilter(super.authenticationManager(), prop, userCacheService, verifyAuthenticationEntryPoint))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                .authenticationEntryPoint(verifyAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and().formLogin().permitAll()
                .and().logout().permitAll()
                .logoutSuccessHandler(logoutSuccessHandler)
        ;

    }

    /**
     * 放过Url
     *
     * @Author YangYaNan
     * @Date Created in 11:34 2022/6/20
     * @Description
     * @param
     * @return
     **/
    String[] permitUrls = new String[]{
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs",
            "/webjars/**",
            "/doc.html",
            "/favicon.ico",
            "/app-manage/**",
            "/error"
    };

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123"));
    }

}
