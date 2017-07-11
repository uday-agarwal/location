package com.talentica.location.fine.sensors.gyroscope;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.talentica.domain.Gyroscope;
import com.talentica.location.fine.sensors.SensorMain;

/**
 * Created by uday.agarwal@signtrace.com on 10-05-2017.
 */

public class GyroscopeSensor implements SensorMain {

    private final Callback callback;
    private final SensorManager sensorManager;
    private final Sensor gyroscope;

    public GyroscopeSensor(Callback callback, Context context) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void start(Activity activity) {
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
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
        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            Log.d("Gyroscope", String.valueOf(event.values[0]) + ", " +
                                String.valueOf(event.values[1]) + ", " +
                                String.valueOf(event.values[2]));

            Gyroscope.Builder builder = new Gyroscope.Builder();
            builder.setXAxis(event.values[0]);
            builder.setYAxis(event.values[1]);
            builder.setZAxis(event.values[2]);

            callback.onUpdateGyroscope(builder.build());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface Callback {
        void onUpdateGyroscope(Gyroscope value);
    }
}
