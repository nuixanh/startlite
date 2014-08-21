package com.clas.starlite.common;

/**
 * Created by sonnt4 on 8/21/2014.
 */
public enum  OperatorType {
    NONE(0),
    AND(1),
    OR(2);
    private final int value;

    private OperatorType(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
