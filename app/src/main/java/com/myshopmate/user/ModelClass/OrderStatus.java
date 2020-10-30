package com.myshopmate.user.ModelClass;

import java.io.Serializable;

public class OrderStatus implements Serializable {
    private String storeId = "";
    private boolean status = false;
    private String cartId = "";
    private boolean paymentSuccess = false;

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "storeId='" + storeId + '\'' +
                ", status=" + status +
                ", cartId='" + cartId + '\'' +
                '}';
    }

    public boolean isPaymentSuccess() {
        return paymentSuccess;
    }

    public void setPaymentSuccess(boolean paymentSuccess) {
        this.paymentSuccess = paymentSuccess;
    }
}
