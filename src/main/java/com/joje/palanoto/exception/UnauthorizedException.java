package com.joje.palanoto.exception;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = -7518080883942510726L;

    public UnauthorizedException() {
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
