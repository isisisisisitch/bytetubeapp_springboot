package com.bytetube.exceptions;

public class ShopOperationException extends RuntimeException {

    private static final long serialVersionUID = -6571206435588752252L;

    public ShopOperationException(String message) {
        super(message);
    }
}
