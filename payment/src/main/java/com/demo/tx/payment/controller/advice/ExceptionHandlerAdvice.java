package com.demo.tx.payment.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public Result base(IllegalArgumentException exception) {
        log.error(ResultCode.PARAM_IS_INVALID.message(), exception);

        return Result.failure(ResultCode.PARAM_IS_INVALID, exception);
    }

}
