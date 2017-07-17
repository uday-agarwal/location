package com.talentica.location.filter;

import com.talentica.domain.DataType;

/**
 * Created by uday.agarwal@talentica.com on 1/30/17.
 */

public interface FilterManager {

    @SuppressWarnings("UnusedReturnValue")
    float process(DataType d, float value);
}
