package com.talentica.location.fine.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import com.talentica.domain.Magnetometer;
import com.talentica.location.filter.FilterManager;
import com.talentica.location.filter.FilterManagerImpl;
import com.talentica.location.fine.SensorMain;

/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public class MagnetometerSensor implements SensorMain  {

    private final Callback callback;
    private final SensorManager sensorManager;
    private final Sensor magnetometer;

    private FilterManager filterManager;

    public MagnetometerSensor(Callback callback, Context context) {
        this.callback = callback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    public void start(Activity activity) {
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
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
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Magnetometer.Builder builder = new Magnetometer.Builder();
            builder.setxAxisRaw(event.values[0]);
            builder.setyAxisRaw(event.values[1]);
            builder.setzAxisRaw(event.values[2]);
            builder.setXAxisFiltered(event.values[0]);
            builder.setYAxisFiltered(event.values[1]);
            builder.setZAxisFiltered(event.values[2]);

            callback.onUpdateMagnetometer(builder.build());
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public interface Callback {
        void onUpdateMagnetometer(Magnetometer value);

    }
}
