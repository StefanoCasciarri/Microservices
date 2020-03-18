package com.bestgroup.conferenceroomservice.conferenceroombooing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BadRequestExeption extends RuntimeException{

    public BadRequestExeption(String s) {
            super(s);
        }

}
