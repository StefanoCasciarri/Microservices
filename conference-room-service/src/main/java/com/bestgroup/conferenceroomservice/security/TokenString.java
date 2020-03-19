package com.bestgroup.conferenceroomservice.security;


public class TokenString {
    private final String value;

    public TokenString(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
