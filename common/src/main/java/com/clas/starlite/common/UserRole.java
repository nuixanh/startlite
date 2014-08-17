package com.clas.starlite.common;

/**
 * Created by Son on 8/17/14.
 */
public enum UserRole {
    ROLE_REGULAR(0),
    ROLE_CONTRIBUTOR(1),
    ROLE_SCENARIO_CREATOR(2);
    private final int value;

    private UserRole(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
