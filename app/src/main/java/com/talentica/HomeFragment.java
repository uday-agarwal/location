package com.talentica;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.talentica.domain.Accelerometer;
import com.talentica.domain.Gyroscope;
import com.talentica.domain.Position;
import com.talentica.location.coarse.CoarseLocation;
import com.talentica.location.coarse.gps.GpsSensor;
import com.talentica.location.fine.FineLocation;
import com.talentica.location.fine.accelerometer.AccelerometerSensor;
import com.talentica.location.fine.gyroscope.GyroscopeSensor;
import com.talentica.locationDetection.R;

/**
 * Created by uday.agarwal@talentica.com on 12-07-2017.
 */

public class HomeFragment extends Fragment implements Sensors {

    private Callback callback;

    private TextView gpsValue;
    private TextView accelerometerValue;
    private TextView gyroscopeValue;

    private CoarseLocation gpsSensor;
    private FineLocation accelerometerSensor;
    private FineLocation gyroscopeSensor;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCallback();
        startSensors();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getView() != null) {
            findViews(getView());
        }
    }

    void findViews(View rootView) {
        gpsValue = (TextView) rootView.findViewById(R.id.gpsValue);
        accelerometerValue = (TextView) rootView.findViewById(R.id.accelerometerValue);
        gyroscopeValue = (TextView) rootView.findViewById(R.id.gyroscopeValue);


    }

    void startSensors() {
        gpsSensor = new GpsSensor(this, getContext());
        accelerometerSensor = new AccelerometerSensor(this, getContext());
        gyroscopeSensor = new GyroscopeSensor(this, getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        gpsSensor.start(getActivity());
        accelerometerSensor.start(getActivity());
        gyroscopeSensor.start(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        gpsSensor.stop();
        accelerometerSensor.stop();
        gyroscopeSensor.stop();
    }

    private void setCallback() {
        if (getParentFragment() instanceof Callback) {
            callback = (Callback) getParentFragment();
        } else if (getActivity() instanceof Callback) {
            callback = (Callback) getActivity();
        } else {
            throw new IllegalArgumentException("Need to implement " + Callback.class.getCanonicalName());
        }
    }

    @Override
    public void onUpdateGPS(Position newPosition) {
        gpsValue.setText(String.valueOf(newPosition.getLatitude()) + ", " + String.valueOf(newPosition.getLongitude()) +
                "\n(Accuracy: " + newPosition.getAccuracy() + " m)");
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

    @Override
    public void permissionNotGranted() {
        Toast.makeText(getContext(), "What!!! No permissions bro??? Duh...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsSensor.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    interface Callback {

    }
}
