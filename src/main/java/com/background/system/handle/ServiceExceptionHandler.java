package com.background.system.handle;

import com.background.system.exception.ServiceException;
import com.background.system.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author menghui.wan
 */
@RestControllerAdvice
@Slf4j
public class ServiceExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public Result<String> handlerException(ServiceException e) {
        return Result.setData(200, null, e.getMessage());
    }
}
