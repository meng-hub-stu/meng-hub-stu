package com.mdx.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

/**
 * 
 * @Title: IMOOCJSONResult.java
 * @Package com.imooc.utils
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 * 
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * 				556: 用户qq校验异常
 * @Copyright: Copyright (c) 2020
 * @Company: www.imooc.com
 * @author 慕课网 - 风间影月
 * @version V1.0
 */
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private T data;
    
    @JsonIgnore
    private String ok;	// 不使用

    public static <T> R<T> build(Integer status, String msg, T data) {
        return new R<>(status, msg, data);
    }

    public static <T> R<T> build(Integer status, String msg, T data, String ok) {
        return new R<>(status, msg, data, ok);
    }
    
    public static <T> R<T> ok(T data) {
        return new R<>(data);
    }

    public static <T> R<T> ok() {
        return new R<>(null);
    }
    
    public static <T> R<T> errorMsg(String msg) {
        return new R<>(500, msg, null);
    }
    
    public static <T> R<T> errorMap(T data) {
        return new R<>(501, "error", data);
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

    public R() {

    }

    public R(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public R(Integer status, String msg, T data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public R(T data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

}
