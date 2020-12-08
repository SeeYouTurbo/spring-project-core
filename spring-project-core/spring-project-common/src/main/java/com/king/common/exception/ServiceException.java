package com.king.common.exception;
/**
 * 服务异常
 *
 * @author jinfeng.wu
 * @date 2020/11/21 17:56
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;
    private final IResultCode resultCode;
    private String[] args;

    /**
     * 适用业务异常, 不需要抛出堆栈信息
     */
    public ServiceException(String message) {
        super(message);
        this.resultCode = ResultCode.INTERFACE_INNER_INVOKE_ERROR;
    }

    public ServiceException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * resultCode 定义的message可预留 String.format
     */
    public ServiceException(IResultCode resultCode, String... args) {
        super(String.format(resultCode.getMessage(), args));
        this.resultCode = resultCode;
        this.args = args;
    }

    public ServiceException(IResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    public Throwable doFillInStackTrace() {
        return super.fillInStackTrace();
    }

    public IResultCode getResultCode() {
        return this.resultCode;
    }

    public String[] getArgs() {
        return this.args;
    }
}
