package com.talentica.location.filter;

import com.talentica.domain.Config;

/**
 * Created by uday.agarwal@talentica.com on 7/17/17.
 */

public final class FilterManagerImpl {

    private static FilterManager filterManagerImpl;

    public static FilterManager init(){
        if(filterManagerImpl ==null){
            synchronized (FilterManagerImpl.class){
                if(filterManagerImpl ==null){
                    switch (Config.FILTER_ALGORITHM){
                        case MEDIAN_FILTER: filterManagerImpl = new MedianFilter();
                            break;
                        default:
                            throw new IllegalArgumentException(Config.FILTER_ALGORITHM.name() + " algorithm is not implemented");
                    }
                }
            }
        }
        return filterManagerImpl;
    }


    private FilterManagerImpl(){

    }


}
