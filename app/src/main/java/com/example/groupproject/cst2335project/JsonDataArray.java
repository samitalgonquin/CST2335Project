package com.example.groupproject.cst2335project;

public class JsonDataArray {
    private String title, phone;
    private double latitude, longitude;

    public JsonDataArray() {
        this("",-1,-1,"");
    }

    public JsonDataArray(String title, double latitude, double longitude, String phone) {
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {return title;}

    public double getLatitude() {return latitude;}

    public double getLongitude() {return longitude;}

    public String getPhone() {return phone;}
}
