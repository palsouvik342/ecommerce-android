package os.com.krishirasayan.classes;


import static os.com.krishirasayan.consts.Config.CODE_REQUEST_FINE_LOCATION;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.w;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import os.com.krishirasayan.R;
import os.com.krishirasayan.interfaces.ICallback;

public class LocationService {

    Context context;
    ICallback successCallback, failureCallback, startListeningCallback;
    LocationManager locationManager;
    Location location;
    boolean listen;
    boolean listenOnce;
    final int LISTEN_MIN_TIME = 2000;
    final int LISTEN_MIN_DIST = 0;

    public LocationService(Context context) {
        this.context = context;
        this.successCallback = null;
        this.failureCallback = null;
        this.startListeningCallback = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        location = null;
        listen = true;
        listenOnce = false;
    }

    public LocationService(Context context, ICallback onLocationGetCallback) {
        this.context = context;
        this.successCallback = onLocationGetCallback;
        this.failureCallback = null;
        this.startListeningCallback = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        location = null;
        listen = true;
        listenOnce = false;
    }

    private void setLocation(Location location) {
        this.location = location;
    }

    private void startListening() {
        listen = true;
    }

    public void stopListening() {
        listen = false;
    }

    public void listen() {
        startListening();
        listenOnce = false;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            i("LocationService: GPS_PROVIDER enabled");
            tryGetLocation();
        } else {
            e( "LocationService: GPS_PROVIDER NOT enabled");
            promptEnableProvider();
        }
    }

    public void listen(ICallback onLocationGetCallback) {
        startListening();
        listenOnce = true;
        this.successCallback = onLocationGetCallback;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            i( "LocationService: GPS_PROVIDER enabled");
            tryGetLocation();
        } else {
            e( "LocationService: GPS_PROVIDER NOT enabled");
            promptEnableProvider();
        }
    }

    public void listenOnce() {
        startListening();
        listenOnce = false;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            i( "LocationService: GPS_PROVIDER enabled");
            tryGetLocation();
        } else {
           e( "LocationService: GPS_PROVIDER NOT enabled");
            promptEnableProvider();
        }
    }

    public void listenOnce(ICallback onLocationGetCallback) {
        startListening();
        listenOnce = true;
        this.successCallback = onLocationGetCallback;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           i("LocationService: GPS_PROVIDER enabled");
            tryGetLocation();
        } else {
            e("LocationService: GPS_PROVIDER NOT enabled");
            promptEnableProvider();
        }
    }

    public void onFailure(ICallback onFailureCallback) {
        this.failureCallback = onFailureCallback;
    }

    public void onStartListening(ICallback onStartListeningCallback) {
        this.startListeningCallback = onStartListeningCallback;
    }

    private void tryGetLocation() {
        String permissionName = Manifest.permission.ACCESS_FINE_LOCATION;
        boolean hasPermission = ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            if(startListeningCallback != null) {
                startListeningCallback.function(null);
            }
            i("LocationService: ACCESS_FINE_LOCATION granted");
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            triggerCallback();
            if(listenOnce) {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        setLocation(location);
                        triggerCallback();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        triggerCallback();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        triggerCallback();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        triggerCallback();
                    }
                }, null);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LISTEN_MIN_TIME, LISTEN_MIN_DIST, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        setLocation(location);
                        triggerCallback();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        triggerCallback();
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        triggerCallback();
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        triggerCallback();
                    }
                });
            }

        } else {
            w( "LocationService: ACCESS_FINE_LOCATION NOT granted");
            Activity activity = (Activity) context;
            activity.requestPermissions(new String[]{permissionName}, CODE_REQUEST_FINE_LOCATION);
            i("LocationService: ACCESS_FINE_LOCATION requested");
        }
    }

    private void triggerCallback() {
        try {
            if (listen && location != null) {
                d( String.format("LocationService UPDATE: %s, %s", String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
                if(successCallback != null) {
                    successCallback.function(location);
                    if(listenOnce) stopListening();
                }
            } else {
                w("LocationService is not Listening or Location NULL");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void promptEnableProvider() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_provider_disabled);
        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_provider_prompt_enable);
        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

}
