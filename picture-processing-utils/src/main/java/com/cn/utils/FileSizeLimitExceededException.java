package com.cn.utils;

public class FileSizeLimitExceededException extends RuntimeException {

    public FileSizeLimitExceededException(String message) {
        super(message);
    }
}
