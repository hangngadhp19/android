package com.example.nganth.restaurantapp;

import java.util.List;

public class DataRestaurant {
    public Result result;

    public class Result {
        public String formatted_address, formatted_phone_number, name, place_id, url;
        public Float rating;
        public List<Photos> photos;

        public class Photos {
            public String photo_reference;
        }

        public Geometry geometry;
        public class Geometry {
            public Location location;

            public class Location {
                public Double lat, lng;
            }
        }
    }
}
