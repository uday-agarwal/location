package com.talentica.location.coarse.gps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.talentica.domain.Position;
import com.talentica.location.coarse.CoarseLocation;


/**
 * Created by uday.agarwal@talentica.com on 05-05-2017.
 */

public class GpsSensor implements CoarseLocation {

    private static final int REQUEST_CODE = 9111;
    private static final int MINIMUM_TIME_INTERVAL = 100;
    private static final int MINIMUM_METER_CHANGE = 0;
    private final static String TAG = "Position ";

    private final Callback callback;
    private LocationManager locationManager;

    public GpsSensor(Callback callback, Context context){
        this.callback = callback;
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void start(Activity activity) {
        Log.d(TAG, "NETWORK_PROVIDER " + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        Log.d(TAG, "GPS_PROVIDER " + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if(checkPermissions(activity)) {
            requestPermissions(activity);
        }else{
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_INTERVAL, MINIMUM_METER_CHANGE, GpsSensor.this);
            }catch (SecurityException e){
                callback.permissionNotGranted();
            }
        }
    }

    private void requestPermissions(Activity activity) {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    private boolean checkPermissions(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void stop() {
        locationManager.removeUpdates(GpsSensor.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Position.Builder builder = new Position.Builder();
        builder.setAccuracy(location.getAccuracy());
        builder.setAltitude(location.getAltitude());
        builder.setLatitude(location.getLatitude());
        builder.setLongitude(location.getLongitude());

        Position newPosition = builder.build();
        callback.onUpdateLocation(newPosition);

        Log.d("Position", location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface Callback{
        void onUpdateLocation(Position newPosition);

        void permissionNotGranted();
    }
}
