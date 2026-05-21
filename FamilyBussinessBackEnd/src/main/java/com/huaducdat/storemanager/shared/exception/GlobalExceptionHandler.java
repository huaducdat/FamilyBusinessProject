package com.huaducdat.storemanager.shared.exception;

import com.huaducdat.storemanager.shared.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<?> handleUnauthorized(
            UnauthorizedException ex
    ) {

        return BaseResponse.fail(
                ex.getMessage()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> handleRuntime(
            RuntimeException ex
    ) {

        return BaseResponse.fail(
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse<?> handleException(
            Exception ex
    ) {

        return BaseResponse.error(
                "Internal Server Error"
        );
    }
}
