package com.myshopmate.user.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

public class VerifyOtp {
    @Json(name = "status") @SerializedName("status") @Expose
    private String status;
    @Json(name = "message") @SerializedName("message") @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "VerifyOtp{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
