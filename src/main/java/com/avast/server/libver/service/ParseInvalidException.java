package com.avast.server.libver.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vitasek L.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParseInvalidException extends RuntimeException {

    public ParseInvalidException() {
    }

    public ParseInvalidException(String message) {
        super(message);
    }

    public ParseInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseInvalidException(Throwable cause) {
        super(cause);
    }

    public ParseInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}