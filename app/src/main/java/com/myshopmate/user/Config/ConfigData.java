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
    private double delivery_charges_primary = 15.00;
    private double delivery_charges_secondary = 25.00;
    private double min_cart_value = 10000;
    private String min_order_value = "1";
    private String max_order_value = "5000";
    private String pivot_delivery_range = "5";
    private String payeeVPA = "";
    private String payeeName = "";
    private String easyUPITransactionDescription = "";

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

    public double getDelivery_charges_primary() {
        return delivery_charges_primary;
    }

    public void setDelivery_charges_primary(double delivery_charges_primary) {
        this.delivery_charges_primary = delivery_charges_primary;
    }

    public double getDelivery_charges_secondary() {
        return delivery_charges_secondary;
    }

    public void setDelivery_charges_secondary(double delivery_charges_secondary) {
        this.delivery_charges_secondary = delivery_charges_secondary;
    }

    public double getMin_cart_value() {
        return min_cart_value;
    }

    public void setMin_cart_value(double min_cart_value) {
        this.min_cart_value = min_cart_value;
    }

    public String getPivot_delivery_range() {
        return pivot_delivery_range;
    }

    public void setPivot_delivery_range(String pivot_delivery_range) {
        this.pivot_delivery_range = pivot_delivery_range;
    }

    public String getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(String min_order_value) {
        this.min_order_value = min_order_value;
    }

    public String getMax_order_value() {
        return max_order_value;
    }

    public void setMax_order_value(String max_order_value) {
        this.max_order_value = max_order_value;
    }

    public String getPayeeVPA() {
        return payeeVPA;
    }

    public void setPayeeVPA(String payeeVPA) {
        this.payeeVPA = payeeVPA;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getEasyUPITransactionDescription() {
        return easyUPITransactionDescription;
    }

    public void setEasyUPITransactionDescription(String easyUPITransactionDescription) {
        this.easyUPITransactionDescription = easyUPITransactionDescription;
    }
}
