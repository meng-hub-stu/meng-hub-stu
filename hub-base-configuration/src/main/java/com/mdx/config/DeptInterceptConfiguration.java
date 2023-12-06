package com.mdx.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.mdx.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * @author Mengdl
 * @date 2023/05/30
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BladeTenantProperties.class)
public class DeptInterceptConfiguration implements ImportAware {

    /**
     * 多租户配置类
     */
    private final BladeTenantProperties properties;

    private String column;

    private String[] value;

    @Bean
    public TenantLineHandler tenantLineHandler() {
        return new BaseTenantHandler(properties);
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(TenantLineHandler tenantLineHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new SystemTenantLineInnerInterceptor(tenantLineHandler));
        // 配置部门权限拦截器
        interceptor.addInnerInterceptor(new DeptLineInnerInterceptor(column, asList(value)));
        // 配置分页拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    @Override
    public void setImportMetadata(AnnotationMetadata annotationMetadata) {
        Map<String, Object> enableAttrs = annotationMetadata.getAnnotationAttributes(EnableDeptInterceptor.class.getName());
        column = valueOf(requireNonNull(enableAttrs).get("column"));
        value = (String[])enableAttrs.get("value");
    }


}
