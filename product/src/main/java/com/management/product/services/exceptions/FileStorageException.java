package com.management.product.services.exceptions;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String msg) {
        super(msg);
    }

    public FileStorageException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
