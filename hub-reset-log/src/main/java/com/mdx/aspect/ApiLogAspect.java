package com.mdx.aspect;

import com.mdx.annotation.ApiLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Mengdl
 * @date 2023/04/26
 */
@Aspect
@Component
@Order(10)
public class ApiLogAspect {
    private static final Logger log = LoggerFactory.getLogger(ApiLogAspect.class);

    @Around("@annotation(apiLog)")
    public Object lockAroundAction(ProceedingJoinPoint point, ApiLog apiLog) throws Throwable {
        //获取类名
        String className = point.getTarget().getClass().getName();
        //获取类上的注解
        ApiLog classLog = point.getTarget().getClass().getAnnotation(ApiLog.class);
        //获取方法
        String methodName = point.getSignature().getName();
        //记录的内容
        String value = apiLog.value();
        //执行时间
        long beginTime = System.currentTimeMillis();
        Object proceed = point.proceed();
        long endTime = System.currentTimeMillis();
        long takeTime = endTime - beginTime;
        //后续可以记录日志
        if (takeTime > 3000) {
            log.error("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        } else {
            log.info("====== 执行结束，耗时：{} 毫秒 ======", takeTime);
        }

        return proceed;
    }

}
