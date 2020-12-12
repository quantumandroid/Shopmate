package com.myshopmate.user.ModelClass;

import com.squareup.moshi.Json;

import java.util.ArrayList;

public class PopularCategoryModel {
    @Json(name = "category_name")
    private String categoryName;
    @Json(name = "category_id")
    private String categoryID;
    @Json(name = "products")
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
