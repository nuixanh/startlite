package com.clas.startlite.webapp.common;

/**
 * Created by Son on 8/14/14.
 */
public enum ErrorCodeMap {
    FAILURE(10000),
    FAILURE_INVALID_PARAMS(1),
    FAILURE_LOGIN_FAIL(2);
    private final int value;

    private ErrorCodeMap(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
