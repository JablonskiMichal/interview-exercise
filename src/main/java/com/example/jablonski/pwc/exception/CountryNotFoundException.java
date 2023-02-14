package com.example.jablonski.pwc.exception;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String country) {
        super(String.format("Country with code %s not found", country));
    }
}
