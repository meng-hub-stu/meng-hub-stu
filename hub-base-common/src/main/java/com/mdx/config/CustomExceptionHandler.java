package com.mdx.config;

import com.google.common.collect.Maps;
import com.mdx.exception.ServiceRunTimeException;
import com.mdx.util.R;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * 自定义异常处理机制
 * @author Mengdl
 * @date 2022/06/15
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 运行时异常处理
     * @param ex 异常类型
     * @return 返回结果
     */
    @ExceptionHandler(value = ServiceRunTimeException.class)
    public R serviceRunTimeExceptionHandler(ServiceRunTimeException ex){
        return R.errorMsg(ex.getMessage());
    }

    /**
     * 处理校验异常处理
     * @param ex 异常类型
     * @return 返回结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, Object> map = Maps.newHashMap();
        for (FieldError fieldError : result.getFieldErrors()) {
            map.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return R.errorMap(map);
    }

}
