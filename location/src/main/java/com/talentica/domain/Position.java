package com.talentica.domain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public final class Position {

    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final float accuracy;

    public Position(Builder builder) {
        latitude = builder.latitude;
        longitude = builder.longitude;
        altitude = builder.altitude;
        accuracy = builder.accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Latitude: ").append(String.valueOf(latitude));
        stringBuilder.append("Longitude: ").append(String.valueOf(longitude));
        stringBuilder.append("Altitude: ").append(String.valueOf(altitude));
        stringBuilder.append("Accuracy: ").append(String.valueOf(accuracy));

        return stringBuilder.toString();
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

        public Position build() {
            return new Position(this);
        }
    }

}
