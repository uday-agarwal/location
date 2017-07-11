package com.talentica.location.fine.sensors;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.talentica.location.fine.FineLocation;

/**
 * Created by uday.agarwal@talentica.com on 04-05-2017.
 */

public interface SensorMain extends FineLocation, SensorEventListener {
}
