package com.example.nganth.restaurantapp;

import java.io.Serializable;

public class Place implements Serializable {

        Double lat,lng;
        String placeId,icon, name, formatted_address, formatted_phone_number, reference;

        public Place(){};
        public Place(String placeId, String icon, String name, String address, String phone, String reference, Double lat, Double lng) {
            this.placeId = placeId;
            this.icon = icon;
            this.name = name;
            this.formatted_address = address;
            this.formatted_phone_number = phone;
            this.reference = reference;
            this.lat = lat;
            this.lng  = lng;
        }

        public String getPlaceId() {
            return placeId;
        }

        public String getName() {
            return name;
        }

        public String getIcon() {
            return icon;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public String getFormatted_phone_number() {
            return formatted_phone_number;
        }

        public String getReference() {
            return reference;
        }

        public Double getLat() {
            return lat;
        }

        public Double getLng() {
            return lng;
        }
}
