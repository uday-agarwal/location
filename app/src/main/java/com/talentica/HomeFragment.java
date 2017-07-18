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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.talentica.domain.Accelerometer;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by uday.agarwal@talentica.com on 12-07-2017.
 */

public class HomeFragment extends Fragment implements Sensors, View.OnClickListener {

    private Callback callback;

    private TextView gpsValue;
    private TextView accelerometerValue;
    private TextView gyroscopeValue;
    private Button logDataButton;
    private Button clearLogsButton;

    private CoarseLocation gpsSensor;
    private FineLocation accelerometerSensor;
    private FineLocation gyroscopeSensor;

    private Boolean isLogEnabled = false;
    private String accelerometerFile;
    private String gyroscopeFile;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCallback();
        initSensors();
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
        logDataButton = (Button) rootView.findViewById(R.id.logButton);
        clearLogsButton = (Button) rootView.findViewById(R.id.clearLogButton);

        logDataButton.setEnabled(true);
        clearLogsButton.setEnabled(true);

        logDataButton.setOnClickListener(this);
        clearLogsButton.setOnClickListener(this);
    }

    void initSensors() {
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

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logButton:
                if(isLogEnabled) {
                    isLogEnabled = false;
                    logDataButton.setText(R.string.logData);
                } else {
                    isLogEnabled = true;
                    createLogFiles();
                    logDataButton.setText(R.string.stopLoggingData);
                }
                break;

            case R.id.clearLogButton:
                clearAllLogs();
                break;

            default:
                break;
        }
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

        checkDiskWritePermissions();
        accelerometerCsv(value);
    }

    @Override
    public void onAccelerometerAccuracyChanged(Sensor sensor, int accuracy) {
        if(getView() != null) {
            Snackbar.make(getView(), "Accelerometer accuracy changed", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateGyroscope(Gyroscope value) {
        gyroscopeValue.setText(String.valueOf(value.getXAxisFiltered()) + "\n" +
                String.valueOf(value.getYAxisFiltered()) + "\n" +
                String.valueOf(value.getZAxisFiltered()));

//        checkDiskWritePermissions();
//        gyroscopeCsv(value);
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

    void checkDiskWritePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(PackageManager.PERMISSION_DENIED == getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission,123);
            }
        }
    }

    void createLogFiles() {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Location logs";
        String accFileName = "Accelerometer-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".csv";
        String accfilePath = baseDir + File.separator + accFileName;
        CSVWriter csvWriter;
        File directory = new File(baseDir);
        directory.mkdir();

        try {
            csvWriter = new CSVWriter(new FileWriter(accfilePath));

            ArrayList<String> row = new ArrayList<>();
            row.add("x Raw");
            row.add("x Filtered");
            row.add("y Raw");
            row.add("y Filtered");
            row.add("z Raw");
            row.add("z Filtered");
            csvWriter.writeNext(row.toArray(new String[0]));
            csvWriter.close();
            Log.d("Log", "Accelerometer log file created");
            accelerometerFile = accfilePath;
        } catch (IOException e) {
            Log.d("File_log", "Failed to create accelerometer log file.");
        }

//        String gyroFileName = "Gyroscope-" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ".csv";
//        String gyroFilePath = baseDir + File.separator + gyroFileName;
//
//        try {
//            csvWriter = new CSVWriter(new FileWriter(gyroFilePath));
//
//            ArrayList<String> row = new ArrayList<>();
//            row.add("x Raw");
//            row.add("x Filtered");
//            row.add("y Raw");
//            row.add("y Filtered");
//            row.add("z Raw");
//            row.add("z Filtered");
//            csvWriter.writeNext(row.toArray(new String[0]));
//            csvWriter.close();
//            Log.d("Log", "Gyroscope log file created");
//            gyroscopeFile = gyroFilePath;
//        } catch (IOException e) {
//            Log.d("File_log", "Failed to create gyroscope log file.");
//        }

    }

    void clearAllLogs() {
        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Location logs";
        File directory = new File(baseDir);

        if(directory.exists()) {
            if(directory.isDirectory()) {
                for(File file: directory.listFiles()) {
                    file.delete();
                }
            }
        }

        if(getView() != null) {
            Snackbar.make(getView(), "All logs cleared.", Snackbar.LENGTH_SHORT).show();
        }
    }

    void accelerometerCsv(Accelerometer value) {
        if(isLogEnabled) {
            CSVWriter csvWriter;
            try {
                csvWriter = new CSVWriter(new FileWriter(accelerometerFile, true));

                ArrayList<String> row = new ArrayList<>();
                row.add(String.valueOf(value.getXAxisRaw()));
                row.add(String.valueOf(value.getXAxisFiltered()));
                row.add(String.valueOf(value.getYAxisRaw()));
                row.add(String.valueOf(value.getYAxisFiltered()));
                row.add(String.valueOf(value.getZAxisRaw()));
                row.add(String.valueOf(value.getZAxisFiltered()));
                csvWriter.writeNext(row.toArray(new String[0]));
                csvWriter.close();
                Log.d("Log", "Log file appended");
            } catch (IOException e) {
                Log.d("File_log", "Failed to log accelerometer data.");
            }
        }
    }

    void gyroscopeCsv(Gyroscope value) {
        if(isLogEnabled) {
            CSVWriter csvWriter;
            try {
                csvWriter = new CSVWriter(new FileWriter(gyroscopeFile, true));

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
                Log.d("File_log", "Failed to log gyroscope data.");
            }
        }
    }

    interface Callback {

    }
}
