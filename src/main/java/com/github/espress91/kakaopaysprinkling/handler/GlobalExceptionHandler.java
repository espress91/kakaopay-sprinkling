package com.github.espress91.kakaopaysprinkling.handler;

import com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult;
import com.github.espress91.kakaopaysprinkling.error.NotFoundException;
import com.github.espress91.kakaopaysprinkling.error.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.github.espress91.kakaopaysprinkling.data.dto.response.ApiResult.ERROR;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ApiResult handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return ERROR(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({ServiceRuntimeException.class, NotFoundException.class})
    protected ApiResult handleServiceRuntimeException(final ServiceRuntimeException e) {
        log.error("handleServiceRuntimeException", e);
        final ApiResult apiResult = ERROR(e, HttpStatus.INTERNAL_SERVER_ERROR);
        return apiResult;
    }

    @ExceptionHandler(Exception.class)
    protected ApiResult handleException(Exception e) {
        log.error("handleException", e);
        return ERROR(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
