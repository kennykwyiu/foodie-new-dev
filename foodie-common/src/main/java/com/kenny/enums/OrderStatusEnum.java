package com.kenny.enums;

public enum OrderStatusEnum {
    WAIT_PAY(10, "To be paid"),
    WAIT_DELIVER(20, "Paid, to be delivered"),
    WAIT_RECEIVE(30, "Shipped, to be received"),
    SUCCESS(40, "Transaction successful"),
    CLOSE(50, "Transaction closed");

    public final Integer type;
    public final String value;

    OrderStatusEnum(Integer type, String value){
        this.type = type;
        this.value = value;
    }
}
