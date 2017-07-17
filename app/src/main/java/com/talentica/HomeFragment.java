package com.talentica;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.talentica.domain.Accelerometer;
import com.talentica.domain.DataType;
import com.talentica.domain.Gyroscope;
import com.talentica.domain.Position;
import com.talentica.location.coarse.CoarseLocation;
import com.talentica.location.coarse.gps.GpsSensor;
import com.talentica.location.fine.FineLocation;
import com.talentica.location.fine.accelerometer.AccelerometerSensor;
import com.talentica.location.fine.gyroscope.GyroscopeSensor;
import com.talentica.locationDetection.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


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
//        gyroscopeSensor.start(getActivity());
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
        accelerometerValue.setText(String.valueOf(value.getXAxisFiltered()) + "\n" +
                String.valueOf(value.getYAxisFiltered()) + "\n" +
                String.valueOf(value.getZAxisFiltered()));

        updateCSV(DataType.ACCELEROMETER, value);
    }

    @Override
    public void onAccelerometerAccuracyChanged(Sensor sensor, int accuracy) {
        if(getView() != null) {
            Snackbar.make(getView(), "Accelerometer accuracy changed", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateGyroscope(Gyroscope value) {
        gyroscopeValue.setText(String.valueOf(value.getXAxis()) + "\n" +
                String.valueOf(value.getYAxis()) + "\n" +
                String.valueOf(value.getZAxis()));
    }

    @Override
    public void permissionNotGranted() {
        Toast.makeText(getContext(), "What!!! No permissions??? Duh...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsSensor.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void updateCSV(DataType d, Accelerometer value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(PackageManager.PERMISSION_DENIED == getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission,123);
            }
        }

        switch(d) {
            case ACCELEROMETER:
                accelerometerCsv(value);
                break;

            default:
                break;
        }
    }

    void accelerometerCsv(Accelerometer value) {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "Accelerometer.csv";
        String filePath = baseDir + File.separator + fileName;
        CSVWriter csvWriter;

        try {
            csvWriter = new CSVWriter(new FileWriter(filePath, true));

            ArrayList<String> row = new ArrayList<>();
            row.add(String.valueOf(value.getXAxisRaw()));
            row.add(String.valueOf(value.getXAxisFiltered()));
            row.add(String.valueOf(value.getYAxisRaw()));
            row.add(String.valueOf(value.getYAxisFiltered()));
            row.add(String.valueOf(value.getZAxisRaw()));
            row.add(String.valueOf(value.getZAxisFiltered()));
            csvWriter.writeNext(row.toArray(new String[0]));
            csvWriter.close();
        } catch (IOException e) {
            Log.d("Calibration_log", "Failed to open log file");
        }

    }

    interface Callback {

    }
}
