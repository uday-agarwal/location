package com.talentica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.talentica.domain.Accelerometer;
import com.talentica.domain.Gyroscope;
import com.talentica.domain.Position;
import com.talentica.location.coarse.CoarseLocation;
import com.talentica.location.coarse.gps.GpsSensor;
import com.talentica.location.fine.FineLocation;
import com.talentica.location.fine.sensors.accelerometer.AccelerometerSensor;
import com.talentica.location.fine.sensors.gyroscope.GyroscopeSensor;
import com.talentica.locationDetection.R;

public class HomeActivity extends AppCompatActivity implements Sensors {

    private TextView gpsValue;
    private TextView accelerometerValue;
    private TextView gyroscopeValue;

    private CoarseLocation gpsSensor;
    private FineLocation accelerometerSensor;
    private FineLocation gyroscopeSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location);
        gpsValue = (TextView) findViewById(R.id.gpsValue);
        accelerometerValue = (TextView) findViewById(R.id.accelerometerValue);
        gyroscopeValue = (TextView) findViewById(R.id.gyroscopeValue);

        gpsSensor = new GpsSensor(this, this);
        accelerometerSensor = new AccelerometerSensor(this, this);
        gyroscopeSensor = new GyroscopeSensor(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gpsSensor.start(this);
        accelerometerSensor.start(this);
        gyroscopeSensor.start(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gpsSensor.stop();
        accelerometerSensor.stop();
        gyroscopeSensor.stop();
    }

    @Override
    public void onUpdateLocation(Position newPosition) {
        gpsValue.setText(String.valueOf(newPosition.getLatitude()) + ", " + String.valueOf(newPosition.getLongitude()) +
                "\n(Accuracy: " + newPosition.getAccuracy() + " m)");
    }

    @Override
    public void permissionNotGranted() {
        Toast.makeText(getApplicationContext(), "What!!! No permissions bro??? Duh...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsSensor.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onUpdateAccelerometer(Accelerometer value) {
        accelerometerValue.setText(String.valueOf(value.getXAxis()) + "\n" +
                                    String.valueOf(value.getYAxis()) + "\n" +
                                    String.valueOf(value.getZAxis()));
    }

    @Override
    public void onUpdateGyroscope(Gyroscope value) {
        gyroscopeValue.setText(String.valueOf(value.getXAxis()) + "\n" +
                                String.valueOf(value.getYAxis()) + "\n" +
                                String.valueOf(value.getZAxis()));
    }
}
