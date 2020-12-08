package com.king.common.exception;
/**
 * 通用错误编码
 *
 * @author jinfeng.wu
 * @date 2020/9/15 9:57
 */
public enum ResultCode implements IResultCode {
    SUCCESS(10000, "成功"),
    PARAM_IS_INVALID(10201, "参数无效"),
    PARAM_IS_BLANK(10202, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10203, "参数类型错误"),
    PARAM_NOT_COMPLETE(10204, "参数缺失"),
    PARAM_NOT_TOKEN(10205, "token缺失"),
    PARAM_TIME_OUT(10206, "时间过期"),
    INTERFACE_INNER_INVOKE_ERROR(10301, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(10302, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(10303, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(10304, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(10305, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(10306, "接口负载过高"),
    INTERFACE_TOKEN_ERROR(10307, "无效token"),
    INTERFACE_TOKEN_EXCEED_TIME(10308, "token失效, 请重新登陆"),
    INTERFACE_RESOURCE_ERROR(10309, "资源操作接口异常"),
    INTERFACE_NOT_SUPPORTED_METHOD_ERROR(10310, "不支持当前请求方法"),
    INTERFACE_NOT_SUPPORTED_MEDIA_ERROR(10311, "不支持当前请求媒体类型"),
    INTERFACE_NOT_AUTHORIZED_ERROR(10312, "未授权"),
    DATA_NOT_EXIST(10313, "数据不存在"),
    DATA_ERROR(10314, "数据错误"),
    SERVER_ERROR(99999, "系统繁忙，请稍后再试");

    final int code;
    final String message;

    @Override
    public int getCode() {
        return this.code;
    }
    @Override
    public String getMessage() {
        return this.message;
    }

    private ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}
