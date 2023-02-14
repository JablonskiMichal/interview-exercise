package com.example.jablonski.pwc.exception;

public class NoPathFoundException extends RuntimeException {

    public NoPathFoundException(String origin, String destination) {
        super(String.format("No routing found between %s and %s", origin, destination));
    }
}
