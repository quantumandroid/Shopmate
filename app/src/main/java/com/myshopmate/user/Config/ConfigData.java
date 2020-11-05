package com.myshopmate.user.Config;

public class ConfigData {
    private String phone_number = "";
    private String start_time = "09:00";
    private String end_time = "20:00";
    private int order_before_time = 18; // hours
    private String app_update = "0";
    private int app_version = 1;
    // Centre Location: Maruti Mandir
    private String centre_lat = "16.990785";
    private String centre_lng = "73.311970";
    private String delivery_range = "5"; // 5 km

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getApp_update() {
        return app_update;
    }

    public void setApp_update(String app_update) {
        this.app_update = app_update;
    }

    @Override
    public String toString() {
        return "ConfigData{" +
                "phone_number='" + phone_number + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", app_update='" + app_update + '\'' +
                '}';
    }

    public int getApp_version() {
        return app_version;
    }

    public void setApp_version(int app_version) {
        this.app_version = app_version;
    }

    public String getCentre_lat() {
        return centre_lat;
    }

    public void setCentre_lat(String centre_lat) {
        this.centre_lat = centre_lat;
    }

    public String getCentre_lng() {
        return centre_lng;
    }

    public void setCentre_lng(String centre_lng) {
        this.centre_lng = centre_lng;
    }

    public String getDelivery_range() {
        return delivery_range;
    }

    public void setDelivery_range(String delivery_range) {
        this.delivery_range = delivery_range;
    }

    public int getOrder_before_time() {
        return order_before_time;
    }

    public void setOrder_before_time(int order_before_time) {
        this.order_before_time = order_before_time;
    }
}
