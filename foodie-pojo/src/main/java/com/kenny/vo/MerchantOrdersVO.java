package com.kenny.vo;

public class MerchantOrdersVO {
    private String merchantOrderId;    // Merchant order ID
    private String merchantUserId;     // Merchant's initiating user's user primary key ID
    private Integer amount;            // Total actual payment amount (including order and postage fees paid by the merchant)
    private Integer payMethod;         // Payment method: 1 - WeChat, 2 - Alipay
    private String returnUrl;          // Callback URL after successful payment (customized by the student)

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
