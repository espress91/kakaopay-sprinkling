package com.github.espress91.kakaopaysprinkling.error;

import org.springframework.http.HttpStatus;

public abstract class ServiceRuntimeException extends RuntimeException{

    private String message;

    private String detail;

    private Object[] params;

    private HttpStatus status;

    public ServiceRuntimeException(String message, String detail, Object[] params, HttpStatus status) {
        this.message = message;
        this.detail = detail;
        this.params = params;
        this.status = status;
    }

    public String getMessage() { return message; }

    public String getDetail() { return detail; }

    public Object[] getParams() {
        return params;
    }

    public HttpStatus getStatus() { return status; }
}
