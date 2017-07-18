package com.talentica.domain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public final class Gyroscope {

    private final double xAxisRaw;
    private final double yAxisRaw;
    private final double zAxisRaw;
    private final double xAxisFiltered;
    private final double yAxisFiltered;
    private final double zAxisFiltered;
    private final float accuracy;

    public Gyroscope(Builder builder) {
        xAxisRaw = builder.xAxisRaw;
        yAxisRaw = builder.yAxisRaw;
        zAxisRaw = builder.zAxisRaw;
        xAxisFiltered = builder.xAxisFiltered;
        yAxisFiltered = builder.yAxisFiltered;
        zAxisFiltered = builder.zAxisFiltered;
        accuracy = builder.accuracy;
    }

    public double getXAxisRaw() {
        return xAxisRaw;
    }

    public double getYAxisRaw() {
        return yAxisRaw;
    }

    public double getZAxisRaw() {
        return zAxisRaw;
    }

    public double getXAxisFiltered() {
        return xAxisFiltered;
    }

    public double getYAxisFiltered() {
        return yAxisFiltered;
    }

    public double getZAxisFiltered() {
        return zAxisFiltered;
    }


    public static final class Builder {
        private double xAxisRaw;
        private double yAxisRaw;
        private double zAxisRaw;
        private double xAxisFiltered;
        private double yAxisFiltered;
        private double zAxisFiltered;
        private float accuracy;

        public void setXAxisRaw(double xAxisRaw) {
            this.xAxisRaw = xAxisRaw;
        }

        public void setYAxisRaw(double yAxisRaw) {
            this.yAxisRaw = yAxisRaw;
        }

        public void setZAxisRaw(double zAxisRaw) {
            this.zAxisRaw = zAxisRaw;
        }

        public void setXAxisFiltered(double xAxisFiltered) {
            this.xAxisFiltered = xAxisFiltered;
        }

        public void setYAxisFiltered(double yAxisFiltered) {
            this.yAxisFiltered = yAxisFiltered;
        }

        public void setZAxisFiltered(double zAxisFiltered) {
            this.zAxisFiltered = zAxisFiltered;
        }

        public void setAccuracy(float accuracy) {
            this.accuracy = accuracy;
        }

        public Gyroscope build() {
            return new Gyroscope(this);
        }
    }

}
