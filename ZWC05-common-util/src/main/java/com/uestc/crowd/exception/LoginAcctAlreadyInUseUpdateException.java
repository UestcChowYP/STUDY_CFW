package com.uestc.crowd.exception;


// 保存和更新时账号重复
public class LoginAcctAlreadyInUseUpdateException extends RuntimeException{
    private static final long serialVersionUID = -1L;

    public LoginAcctAlreadyInUseUpdateException() {
    }

    public LoginAcctAlreadyInUseUpdateException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseUpdateException(Throwable cause) {
        super(cause);
    }

    public LoginAcctAlreadyInUseUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
