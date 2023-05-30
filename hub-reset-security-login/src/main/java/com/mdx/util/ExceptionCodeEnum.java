package com.mdx.util;


/**
 * BusinessException结果集枚举
 *
 * @author gxb
 */
public enum ExceptionCodeEnum {

    SUCCESS(200, "请求成功"),
    SERVICE_ERROR(500, "程序开小差了"),
    PROCESSOR_NOT_FOUND(501, "File handler not found"),
    ACCESS_ERROR(503, "当前访问流量过大，请稍后重试"),

    // 2xxx 业务异常
    BIZ_ERROR(2000, "业务操作失败"),
    ILLEGAL_OPERATION_ERROR(2001, "非正常操作提示"),
    SOME_DATA_FAILS(2021, "部分数据失败"),
    CHANNEL_STATUS_ERROR(2030, "渠道已下线"),
    // 不告警
    BIZ_ERROR_NOT_ALERT(2999, "业务操作失败"),
    FILE_FORMAT_ERROR(2008, "文件格式错误"),
    IDENTIFICATION_OF_FAILURE(2012, "识别失败"),
    REPEAT_EXECUTE_SERVICE(2013, "重复提交"),
    PAYMENT_EXECUTE_SERVICE(2007, "支付补充"),
    PAYMENT_EXECUTE_DOUBLE_RECORD(2014, "深圳需要双录"),
    CONFIG_NOT_FOUND(2015, "配置信息找不到"),
    FURTHER_EXECUTE_SERVICE(2009, "需要补充资料"),
    FILE_TOO_LARGE(2010, "文件过大"),
    PULL_PAYMENT_LINK_FAILURE(2011, "请求支付链接失败"),

    // 4xxx 客户端异常
    BAD_REQUEST(4001, "非法请求"),
    REPEAT_EXECUTE(4002, "请勿重复提交"),
    FORBIDDEN_OPERATION(4003, "无权限操作"),
    TIMEOUT(4004, "请求超时"),
    UNAUTHORIZED_ACCESS(401, "未找到当前登录用户信息"),
    ERROR_TOKEN(4006,"错误的TOKEN!"),
    CONFLICT_TOKEN(4024,"在别处已登录!"),
    EXPIRED_TOKEN(4007,"由于长时间未操作,请重新操作!"),
    NETWORK_ERROR(4008,"网络异常"),
    UNAUTHORIZED_SIGN(4009,"签名校验失败"),
    DECRYPT_FAIL(4010,"解密失败"),
    ENCRYPT_FAIL(4011,"加密失败"),

    // 5xxx 第三方服务异常
    RPC_ERROR(5000, "调用远程接口失败"),

    // 6xxx 基础中间件异常
    DB_ERROR(6000, "数据库操作失败"),

    // 7xxx 数据问题
    PARAM_ERROR(7000, "参数错误"),
    NOT_EXIST(7001, "请求数据不存在"),
    DATA_NULL(7002, "没有数据"),
    OPERATE_NOT_ALLOW(7003, "操作不允许"),
    EXIST(7004, "请求数据已存在"),
    LINK_USELESSNESS(7005, "链接已失效");


    /**
     * 获取code
     *
     * @return int code
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取desc
     *
     * @return string desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param code 代码
     * @param desc 详情
     */
    ExceptionCodeEnum(final int code, final String desc) {
        this.code = code;
        this.desc = desc;
    }

    private int code;
    private String desc;

}
