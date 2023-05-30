package com.mdx.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应类
 * @param <T>
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Integer status;

    private String msg;

    private T data;

    public R(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok(Integer status, String msg, T data) {
        return new R<>(status, msg, data);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "操作成功", data);
    }

    public static <T> R<T> ok() {
        return new R<>(200, "操作成功", null);
    }
    
    public static <T> R<T> errorMsg(String msg) {
        return new R<>(500, msg, null);
    }
    
    public static <T> R<T> errorMap(T data) {
        return new R<>(501, "error", data);
    }
    public static <T> R<T> fail(String msg) {
        return new R<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static<T> R<T> of(Integer status, String msg) {
        return new R<>(status, msg, null);
    }

    public Boolean isOK() {
        return this.status == 200;
    }
    
    public static <T> R<T> errorTokenMsg(String msg) {
        return new R(502, msg, null);
    }
    
    public static <T> R<T> errorException(String msg) {
        return new R<>(555, msg, null);
    }
    
    public static <T> R<T> errorUserQQ(String msg) {
        return new R<>(556, msg, null);
    }

}
