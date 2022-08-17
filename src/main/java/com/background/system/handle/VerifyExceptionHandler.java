package com.background.system.handle;

import com.background.system.exception.VerifyException;
import com.background.system.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 校验异常处理器
 */
@RestControllerAdvice
@Slf4j
public class VerifyExceptionHandler {

    @ExceptionHandler(VerifyException.class)
    public Result<String> handlerException(VerifyException e) {
        return Result.setData(200, null, e.getExceptionMsg());
    }

}
