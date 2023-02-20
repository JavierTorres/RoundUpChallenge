package com.javiertorres.StarlingAPIChallenge.domain.exception;

public class NotPrimaryAccountException extends RuntimeException {
    public NotPrimaryAccountException() {
        super("Not primary account found");
    }
}
