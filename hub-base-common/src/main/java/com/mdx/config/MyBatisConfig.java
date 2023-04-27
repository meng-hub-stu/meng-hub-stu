package com.mdx.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mengdl
 * @date 2023/04/25
 */
@Configuration
@MapperScan({"com.mdx.mapper.**"})
//@ComponentScan(basePackages={"cn.hutool.extra.spring", "com.mdx"})
public class MyBatisConfig {
}
