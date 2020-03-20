package com.bestgroup.userservice.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class OtherServiceNotRespondingException extends RuntimeException {
    public OtherServiceNotRespondingException(String s) {
        super(s);
    }
}