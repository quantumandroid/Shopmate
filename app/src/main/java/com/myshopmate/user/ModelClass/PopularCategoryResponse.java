package com.myshopmate.user.ModelClass;

import com.squareup.moshi.Json;

import java.util.ArrayList;

public class PopularCategoryResponse {
    @Json(name = "status")
    private boolean status;
    @Json(name = "data")
    private ArrayList<PopularCategoryModel> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<PopularCategoryModel> getData() {
        return data;
    }

    public void setPopularCategoryModels(ArrayList<PopularCategoryModel> data) {
        this.data = data;
    }
}
