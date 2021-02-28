package com.example.nganth.restaurantapp;

import java.io.Serializable;

public class Restaurant implements Serializable {
    public String resId, resName, resAddress, resImage, userEmail;
    public Float resRate;


    private boolean selected;

    public Restaurant(String resId, String resName, String resAddress, String resImage, String userEmail, Float resRate) {
        this.resId = resId;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resImage = resImage;
        this.userEmail = userEmail;
        this.resRate = resRate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResImage() {
        return resImage;
    }

    public void setResImage(String resImage) {
        this.resImage = resImage;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Float getResRate() {
        return resRate;
    }

    public void setResRate(Float resRate) {
        this.resRate = resRate;
    }
}
