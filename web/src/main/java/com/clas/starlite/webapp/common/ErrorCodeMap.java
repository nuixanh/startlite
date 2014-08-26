package com.clas.starlite.webapp.common;

/**
 * Created by Son on 8/14/14.
 */
public enum ErrorCodeMap {
    FAILURE_EXCEPTION(-1),
    FAILURE_INVALID_PARAMS(1),
    FAILURE_SESSION_INVALID(2),
    FAILURE_LOGIN_FAIL(3),
    FAILURE_OBJECT_NOT_FOUND(4),
    FAILURE_PERMISSION_DENY(5),
    FAILURE_SCENARIO_NOT_FOUND(6),
    FAILURE_SOLUTION_NOT_FOUND(7),
    FAILURE_INVALID_EMAIL(8),
    FAILURE_USER_NOT_FOUND(9),
    FAILURE_SECTION_NOT_FOUND(10),
    FAILURE_SECTION_BELONG_SAME_ROOT_SCENARIO(11),
    FAILURE_EMAIL_EXISTED(12),
    FAILURE_INVALID_CONDITIONS(13),
    FAILURE_DUPLICATED_NAME(14);

    private final int value;

    private ErrorCodeMap(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
