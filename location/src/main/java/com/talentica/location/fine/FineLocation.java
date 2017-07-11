package com.talentica.location.fine;

import android.app.Activity;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public interface FineLocation {

    void start(Activity activity);

    void stop();

    void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
}
