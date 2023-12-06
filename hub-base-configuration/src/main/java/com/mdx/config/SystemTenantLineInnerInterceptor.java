package com.mdx.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Mengdl
 * @date 2023/12/06
 */
@Slf4j
public class SystemTenantLineInnerInterceptor extends TenantLineInnerInterceptor {
    private static final ThreadLocal<String> IGNORE_TENANT = new ThreadLocal<>();

    private static final String DEFAULT_TENANT_ID = "000000";

    public SystemTenantLineInnerInterceptor() {

    }

    public SystemTenantLineInnerInterceptor(final TenantLineHandler tenantLineHandler) {
        super(tenantLineHandler);
    }


}
