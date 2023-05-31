package com.mdx.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Mengdl
 * @date 2023/05/30
 */
@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Import(DeptInterceptConfiguration.class)
public @interface EnableDeptInterceptor {
    /**
     * 所属部门数据表
     * @return 所属部门数据表
     */
    String[] value() default "";

    /**
     * 所属部门字段名称
     * @return 所属部门字段名称
     */
    String column() default "dept_id";

}
