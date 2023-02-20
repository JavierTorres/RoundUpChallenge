package com.javiertorres.StarlingAPIChallenge.domain.exception;

public class StarlingClientException extends RuntimeException {
    public StarlingClientException(String message) {
        super(message);
    }
}
