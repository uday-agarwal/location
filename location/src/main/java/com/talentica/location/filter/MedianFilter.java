package com.talentica.location.filter;


import com.talentica.domain.DataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uday.agarwal@talentica.com on 1/30/17.
 */

final class MedianFilter implements FilterManager {

    private static final int MAX_MEDIAN_HISTORY = 9;
    private final Map<DataType, ArrayList<Float>> data;

    MedianFilter() {
        data = new HashMap<>();
    }

    @Override
    public float process(DataType d, float raw) {
        insertInMedianRssiMap(d, raw);

        return runMedianFilter(d);
    }

    private float runMedianFilter(DataType d) {
        ArrayList<Float> computationList = new ArrayList<>(data.get(d).size());
        computationList.addAll(data.get(d));
        Collections.sort(computationList);
        int index = (computationList.size() / 2);
        return computationList.get(index);
    }

    private void insertInMedianRssiMap(DataType d, float raw){
        if(!data.containsKey(d)) {
            data.put(d, new ArrayList<Float>());
            data.get(d).add(raw);
        } else {
            ArrayList<Float> list = data.get(d);
            list.add(raw);
            if(list.size() > MAX_MEDIAN_HISTORY) {
                list.remove(0);
            }
        }
    }
}
