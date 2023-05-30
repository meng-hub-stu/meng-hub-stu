package com.mdx.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常规异常
 * @author austinChen
 * Created by austinChen on 2019/6/25 10:08.
 */
@Getter
@Setter
@Slf4j
public class BusException extends  RuntimeException {

    /**
     * 非法参数
     */
    public static final int ILLEGAL_ARGS =1;

    /**
     * 非法Ip
     */
    public static final int ILLEGAL_IP =2;

    /**
     * 无效授权
     */
    public static final int ILLEGAL_AUTH =3;

    /**
     * 签名有误
     */
    public static final int ILLEGAL_SIGN =4;

    /**
     * 不存在的方法或者接口
     */
    public static final int NOT_EXIST_METHOD =5;

    /**
     * 超过限制次数
     */
    public static final int TIME_LIMIT =6;

    /**
     * 调用过于频繁
     */
    public static final int FREQ_CALL =7;

    /**
     * 资源不存在
     */
    public static final int NOT_EXIST_RESOURCE =8;

    /**
     * 未知错误
     */
    public static final int UNKNOWN =9;


    /**
     * 数据库异常
     */
    public static final int DB_ERROR =1001;

    /**
     * 代码错误
     */
    public static final int CODE_ERROR =1002;

    /**
     * 网络错误
     */
    public static final int NET_ERROR =1003;

    /**
     * 错误代码
     */
    private final int code;

    /**
     * 详细的多项错误描述
     */
    private final Map<String,String> remarks=new HashMap<>();

    protected  static final Map<Integer,String> UNIQUE_REMARK =new HashMap<>();

    static
    {
        UNIQUE_REMARK.put(ILLEGAL_ARGS,"非法参数");
        UNIQUE_REMARK.put(ILLEGAL_IP,"非法Ip");
        UNIQUE_REMARK.put(ILLEGAL_AUTH,"无效授权");
        UNIQUE_REMARK.put(ILLEGAL_SIGN,"签名有误");
        UNIQUE_REMARK.put(NOT_EXIST_METHOD,"不存在的方法或者接口");

        UNIQUE_REMARK.put(TIME_LIMIT,"超过限制次数");
        UNIQUE_REMARK.put(FREQ_CALL,"调用过于频繁");
        UNIQUE_REMARK.put(NOT_EXIST_RESOURCE,"资源不存在");
        UNIQUE_REMARK.put(UNKNOWN,"未知错误");

        UNIQUE_REMARK.put(DB_ERROR,"数据库异常");
        UNIQUE_REMARK.put(CODE_ERROR,"代码错误");
        UNIQUE_REMARK.put(NET_ERROR,"网络错误");
    }

    /**
     * @param code 错误代码
     * @param msg 错误信息
     */
    public BusException(int code, String msg)
    {
        super(msg);
        this.code=code;
    }

    public BusException(int code, String msg, Map<String,String> remarks)
    {
        super(msg);
        this.code=code;
        if(!CollectionUtils.isEmpty(remarks)) {
            this.remarks.putAll(remarks);
        }
    }

    public static BusException gen(int code)
    {
        String remark= UNIQUE_REMARK.getOrDefault(code,"找不到异常描述");
        return new BusException(code,remark);
    }

    /**
     * @param ex 异常信息
     */
    public BusException(Exception ex)
    {
        super(ex.getMessage());
        this.code= ILLEGAL_ARGS;
    }


    /**
     * @param msg 默认提示未非法参数
     */
    public BusException(String msg)
    {
        super(msg);
        this.code= ILLEGAL_ARGS;
    }

}
