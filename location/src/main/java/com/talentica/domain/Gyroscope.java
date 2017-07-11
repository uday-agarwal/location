package com.talentica.domain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public final class Gyroscope {

    private final double xAxis;
    private final double yAxis;
    private final double zAxis;
    private final float accuracy;

    public Gyroscope(Builder builder) {
        xAxis = builder.xAxis;
        yAxis = builder.yAxis;
        zAxis = builder.zAxis;
        accuracy = builder.accuracy;
    }

    public double getXAxis() {
        return xAxis;
    }

    public double getYAxis() {
        return yAxis;
    }

    public double getZAxis() {
        return zAxis;
    }


    public static final class Builder {
        private double xAxis;
        private double yAxis;
        private double zAxis;
        private float accuracy;


        public void setXAxis(double xAxis) {
            this.xAxis = xAxis;
        }

        public void setYAxis(double yAxis) {
            this.yAxis = yAxis;
        }

        public void setZAxis(double zAxis) {
            this.zAxis = zAxis;
        }

        public void setAccuracy(float accuracy) {
            this.accuracy = accuracy;
        }

        public Gyroscope build() {
            return new Gyroscope(this);
        }
    }

}
