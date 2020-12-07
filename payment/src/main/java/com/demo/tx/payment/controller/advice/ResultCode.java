package com.demo.tx.payment.controller.advice;

public enum ResultCode {
    /* success state code */
    SUCCESS(1, "SUCCESS"),

    /* parameter error: 1001 - 19999 */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),

    /* user error: 2001 - 29999 */
    USER_NOT_LOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "用户不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被占用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXIST(2005, "用户已存在");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return code;
    }

    public String message() {
        return message;
    }
}
