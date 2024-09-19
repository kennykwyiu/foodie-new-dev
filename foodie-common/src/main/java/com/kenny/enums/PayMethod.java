package com.kenny.enums;

public enum PayMethod {
    WEIXIN(1, "WECHAT"),
    ALIPAY(2, "ALIPAY");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
