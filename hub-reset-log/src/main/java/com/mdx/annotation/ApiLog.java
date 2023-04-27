package com.mdx.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author Mengdl
 * @date 2023/04/26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Order(10)
public @interface ApiLog {

    /**
     * 日志描述
     * @return 返回信息
     */
    String value()  default "";

}
