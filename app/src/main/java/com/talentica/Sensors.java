package com.talentica;

import com.talentica.location.coarse.gps.GpsSensor;
import com.talentica.location.fine.accelerometer.AccelerometerSensor;
import com.talentica.location.fine.gyroscope.GyroscopeSensor;
import com.talentica.location.fine.magnetometer.MagnetometerSensor;

/**
 * Created by uday.agarwal@talentica.com on 10-05-2017.
 */

public interface Sensors extends GpsSensor.Callback,
                                 AccelerometerSensor.Callback,
                                 GyroscopeSensor.Callback,
                                 MagnetometerSensor.Callback {
}
