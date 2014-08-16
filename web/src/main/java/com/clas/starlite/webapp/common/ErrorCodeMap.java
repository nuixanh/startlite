package com.clas.starlite.webapp.common;

/**
 * Created by Son on 8/14/14.
 */
public enum ErrorCodeMap {
    FAILURE_EXCEPTION(-1),
    FAILURE_INVALID_PARAMS(1),
    FAILURE_SESSION_INVALID(2),
    FAILURE_LOGIN_FAIL(3),
    FAILURE_OBJECT_NOT_FOUND(4);
    private final int value;

    private ErrorCodeMap(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
