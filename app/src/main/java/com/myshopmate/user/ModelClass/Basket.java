package com.myshopmate.user.ModelClass;

import com.myshopmate.user.Adapters.ImageAdapterData;

public class Basket {
    private ImageAdapterData imageAdapterData;
    private String storeName = "";
    private String totalPrice = "";

    public ImageAdapterData getImageAdapterData() {
        return imageAdapterData;
    }

    public void setImageAdapterData(ImageAdapterData imageAdapterData) {
        this.imageAdapterData = imageAdapterData;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
