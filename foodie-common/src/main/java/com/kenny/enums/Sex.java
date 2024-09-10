package com.kenny.enums;

public enum Sex {
    woman(0, "female"),
    man(1, "male"),
    secret(2, "secret");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
