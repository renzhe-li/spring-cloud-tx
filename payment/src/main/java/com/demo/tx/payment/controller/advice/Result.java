package com.demo.tx.payment.controller.advice;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public Result(ResultCode resultCode, Object data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.data = data;
    }

    protected void setResultCode(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public static Result success() {
        final Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);

        return result;
    }

    public static Result success(Object o) {
        final Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(o);

        return result;
    }

    public static Result failure(ResultCode resultCode) {
        final Result result = new ErrorResult();
        result.setResultCode(resultCode);

        return result;
    }

    public static Result failure(ResultCode resultCode, String errors) {
        final ErrorResult result = new ErrorResult();
        result.setResultCode(resultCode);
        result.setErrors(errors);

        return result;
    }

    public static Result failure(Integer code, String message, String errors) {
        final ErrorResult result = new ErrorResult();

        result.setCode(code);
        result.setMessage(message);
        result.setErrors(errors);

        return result;
    }

    @Setter
    @Getter
    public static class ErrorResult extends Result {
        private String errors;
    }

}
