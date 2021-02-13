package com.myshopmate.user.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewCategoryVarientList implements Serializable {

    @SerializedName("varient_id") @Expose
    private String varient_id;
    @SerializedName("description") @Expose
    private String description;
    @SerializedName("price") @Expose
    private String price;
    @SerializedName("mrp") @Expose
    private String mrp;
    @SerializedName("varient_image") @Expose
    private String varient_image;
    @SerializedName("unit") @Expose
    private String unit;
    @SerializedName("quantity") @Expose
    private String quantity;
    @SerializedName("deal_price") @Expose
    private String deal_price;
    @SerializedName("valid_from") @Expose
    private String valid_from;
    @SerializedName("valid_to") @Expose
    private String valid_to;
    @SerializedName("store_id") @Expose
    private String store_id;
    @SerializedName("stock") @Expose
    private String stock;
    @SerializedName("in_stock") @Expose
    private String in_stock;

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(String deal_price) {
        this.deal_price = deal_price;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(String in_stock) {
        this.in_stock = in_stock;
    }
}
