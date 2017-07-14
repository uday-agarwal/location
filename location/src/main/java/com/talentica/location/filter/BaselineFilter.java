package com.talentica.location.filter;


/**
 * Created by uday.agarwal@talentica.com on 1/30/17.
 */

final class BaselineFilter implements FilterManager {

    private static final float VARIANCE_THRESHOLD = 6;
    private static final int LOW_RESET_COUNT_LIMIT = 7;
    private static final int HIGH_RESET_COUNT_LIMIT = 4;
    private static final int BUCKETING_FACTOR = 8;

    @Override
    public float process(float raw) {
        raw /= 2;
        return runBaselineFilter(raw);
    }

    private float runBaselineFilter(float raw) {
        raw *= 2;
        return raw;
    }

}
