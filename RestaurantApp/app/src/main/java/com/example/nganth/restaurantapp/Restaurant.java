package com.example.nganth.restaurantapp;

import java.io.Serializable;

/**
 * Created by HV on 5/30/2018.
 */

public class Restaurant implements Serializable {
    public String resName, resAddress, resImage;

    public Restaurant(String _resName, String _resAddress, String _resImage){
        resName = _resName;
        resAddress = _resAddress;
        resImage = _resImage;
    }
}
