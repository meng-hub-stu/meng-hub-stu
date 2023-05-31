package com.mdx;

import com.mdx.config.EnableDeptInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mengdl
 * @date 2023/05/30
 */
@EnableDeptInterceptor(/*{"hub_dept"}*/)
@SpringBootApplication
public class ConfigurationApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationApplication.class, args);
    }

}
