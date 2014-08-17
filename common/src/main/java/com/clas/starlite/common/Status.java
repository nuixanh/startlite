package com.clas.starlite.common;

/**
 * Created by Son on 8/17/14.
 */
public enum Status {
    DEACTIVE(-1),
    PENDING(0),
    ACTIVE(3);
    private final int value;

    private Status(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
