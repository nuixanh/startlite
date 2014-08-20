package com.clas.starlite.common;

/**
 * Created by Son on 8/19/14.
 */
public enum QuestionType {
    ONE_CHOICE(0),
    MULTI_CHOICE(1);

    private final int value;

    private QuestionType(int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
