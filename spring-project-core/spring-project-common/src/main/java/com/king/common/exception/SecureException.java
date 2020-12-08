package com.king.common.exception;

public class SecureException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;

    private final IResultCode resultCode;

    public IResultCode getResultCode() {
        return this.resultCode;
    }

    public SecureException(String message) {
        super(message);
        this.resultCode = (IResultCode)ResultCode.INTERFACE_INNER_INVOKE_ERROR;
    }

    public SecureException(IResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public SecureException(IResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
