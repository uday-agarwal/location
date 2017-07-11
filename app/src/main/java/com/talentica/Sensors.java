package com.talentica;

import com.talentica.location.coarse.gps.GpsSensor;
import com.talentica.location.fine.sensors.accelerometer.AccelerometerSensor;
import com.talentica.location.fine.sensors.gyroscope.GyroscopeSensor;

/**
 * Created by uday.agarwal@signtrace.com on 10-05-2017.
 */

public interface Sensors extends GpsSensor.Callback, AccelerometerSensor.Callback, GyroscopeSensor.Callback {
}
