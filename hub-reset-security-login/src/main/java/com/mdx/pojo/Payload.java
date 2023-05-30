package com.mdx.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author ：YangYaNan
 * @date ：Created in 16:30 2022/4/11
 * @description ：
 * @version: 1.0
 */
@Data
public class Payload<T> {
    private String id;
    private T userInfo;
    private Date expiration;
}
