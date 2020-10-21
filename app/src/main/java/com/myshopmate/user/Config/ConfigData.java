package com.myshopmate.user.Config;

public class ConfigData {
    private String phone_number = "";
    private String start_time = "09:00";
    private String end_time = "20:00";
    private String app_update = "0";
    private int app_version = 1;

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
}
