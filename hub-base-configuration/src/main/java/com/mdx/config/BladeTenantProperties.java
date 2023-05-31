package com.mdx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mengdl
 * @date 2023/05/31
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "blade.tenant")
public class BladeTenantProperties {
    /**
     * 多租户字段名称
     */
    private String column = "tenant_id";

    /**
     * 多租户数据表
     */
    private List<String> tables = new ArrayList<>();

    /**
     * 多租户系统数据表
     */
    private List<String> bladeTables = Arrays.asList("blade_notice", "blade_post", "blade_log_api", "blade_log_error", "blade_log_usual");

}
