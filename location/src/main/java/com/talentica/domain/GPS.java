package com.talentica.domain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public final class GPS {

    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final float accuracy;

    public GPS(Builder builder) {
        latitude = builder.latitude;
        longitude = builder.longitude;
        altitude = builder.altitude;
        accuracy = builder.accuracy;
    }


    public static final class Builder {
        private double latitude;
        private double longitude;
        private double altitude;
        private float accuracy;


        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setAltitude(double altitude) {
            this.altitude = altitude;
        }

        public void setAccuracy(float accuracy) {
            this.accuracy = accuracy;
        }

        public GPS build() {
            return new GPS(this);
        }
    }

}
