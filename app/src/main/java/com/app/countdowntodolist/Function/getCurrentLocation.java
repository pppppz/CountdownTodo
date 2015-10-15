package com.app.countdowntodolist.Function;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class getCurrentLocation {


    public static LatLng latlng;
    private static String TAG = "getCurrentLocation";

    public static Location get(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = null;
        int MIN_TIME_BW_UPDATES = 100;
        int MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Log.e(TAG, "gps and network not work");
        } else {
            Log.e(TAG, "can get location");
            if (isNetworkEnabled) {

                try {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            //   latlng = new LatLng(location.getLatitude(), location.getLongitude());
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
                    });

                    Log.e(TAG, "Can get location by network");


                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                } catch (SecurityException se) {

                    Log.e("ptest", "error : " + se);
                }

            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    try {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, new android.location.LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                latlng = new LatLng(location.getLatitude(), location.getLongitude());
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
                        });
                        Log.d("GPS Enabled", "GPS Enabled");

                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                    } catch (SecurityException e) {
                        Log.d("Get Location", "error : " + e);
                    }
                }
            }
        }
        return location;
    }
}
