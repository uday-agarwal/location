package com.talentica.location.fine.sensors.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.talentica.domain.Accelerometer;
import com.talentica.location.fine.sensors.SensorMain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public class AccelerometerSensor implements SensorMain  {

    private final Callback callback;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;

    public AccelerometerSensor(Callback callback, Context context) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    @Override
    public void start(Activity activity) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
            Log.d("Accelerometer", String.valueOf(event.values[0]) + ", " + String.valueOf(event.values[1]) + ", " + String.valueOf(event.values[2]));

            Accelerometer.Builder builder = new Accelerometer.Builder();
            builder.setXAxis(event.values[0]);
            builder.setYAxis(event.values[1]);
            builder.setZAxis(event.values[2]);

            callback.onUpdateAccelerometer(builder.build());}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface Callback {
        void onUpdateAccelerometer(Accelerometer value);
    }
}
