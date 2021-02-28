package com.example.nganth.restaurantapp;

import java.io.Serializable;

public class Foods implements Serializable {
    public String resName, foodId, name, image;

    public Foods(String resName, String foodId, String name, String image) {
        this.resName = resName;
        this.foodId = foodId;
        this.name = name;
        this.image = image;
    }

    public Foods() {

    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
