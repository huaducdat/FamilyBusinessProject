package com.huaducdat.storemanager.exception;

import com.huaducdat.storemanager.model.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleRuntime(
            RuntimeException ex
    ) {

        return BaseResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleException(
            Exception ex
    ) {

        return BaseResponse.builder()
                .success(false)
                .message("Internal Server Error")
                .data(null)
                .build();
    }
}