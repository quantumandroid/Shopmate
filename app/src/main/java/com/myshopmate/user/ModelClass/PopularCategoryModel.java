package com.myshopmate.user.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularCategoryModel {
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_id")
    @Expose
    private String categoryID;
    @SerializedName("products")
    @Expose
    private ArrayList<NewCategoryDataModel> products;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public ArrayList<NewCategoryDataModel> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<NewCategoryDataModel> products) {
        this.products = products;
    }
}
