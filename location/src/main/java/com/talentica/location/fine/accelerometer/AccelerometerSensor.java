package com.talentica.location.fine.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.talentica.domain.Accelerometer;
import com.talentica.location.filter.FilterManager;
import com.talentica.location.filter.FilterManagerImpl;
import com.talentica.location.fine.SensorMain;

import com.talentica.domain.DataType;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public class AccelerometerSensor implements SensorMain  {

    private final Callback callback;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;

    private FilterManager filterManager;

    public AccelerometerSensor(Callback callback, Context context) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    public void start(Activity activity) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        filterManager = FilterManagerImpl.init();
    }

    @Override
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            Accelerometer.Builder builder = new Accelerometer.Builder();
            builder.setxAxisRaw(event.values[0]);
            builder.setyAxisRaw(event.values[1]);
            builder.setzAxisRaw(event.values[2]);
            builder.setXAxisFiltered(filterManager.process(DataType.ACCELEROMETER_0, event.values[0]));
            builder.setYAxisFiltered(filterManager.process(DataType.ACCELEROMETER_1, event.values[1]));
            builder.setZAxisFiltered(filterManager.process(DataType.ACCELEROMETER_2, event.values[2]));

            callback.onUpdateAccelerometer(builder.build());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        callback.onAccelerometerAccuracyChanged(sensor, accuracy);
    }

    public interface Callback {
        void onUpdateAccelerometer(Accelerometer value);
        void onAccelerometerAccuracyChanged(Sensor sensor, int accuracy);
    }
}
