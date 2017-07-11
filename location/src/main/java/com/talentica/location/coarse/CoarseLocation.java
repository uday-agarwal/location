package com.talentica.location.coarse;

import android.app.Activity;
import android.location.LocationListener;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public interface CoarseLocation extends LocationListener {

    void start(Activity activity);

    void stop();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
