package com.clas.starlite.common;

/**
 * Created by sonnt4 on 8/21/2014.
 */
public enum  RuleType {
    INCLUDE(1),
    EXCLUDE(2);
    private final int value;

    private RuleType(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
