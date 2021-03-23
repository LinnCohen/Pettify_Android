package com.pettify.Utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.pettify.model.PettifyApplication;

public final class LocationUtils implements LocationListener {
    public final static LocationUtils instance = new LocationUtils();
    LocationManager locationManager;
    double lat = 0.0;
    double lng = 0.0;

    private LocationUtils() {
        locationManager = (LocationManager) PettifyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Log.d("location", "Locatoin !=null");
            setLat(location.getLatitude());
            setLng(location.getLongitude());
        }


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("New location", location.toString());
        setLat(location.getLatitude());
        setLng(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public String getCurrentLocationAsString() {
        locationManager = (LocationManager) PettifyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
             TODO: Consider calling
                ActivityCompat#requestPermissions
             here to request the missing permissions, and then overriding
               public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                      int[] grantResults)
             to handle the case where the user grants the permission. See the documentation
             for ActivityCompat#requestPermissions for more details.
            */
            Log.d("location", "Client does not have location/network permmisons please active permissions");
            return null;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            setLat(location.getLatitude());
            setLng(location.getLongitude());
            LatLng myCoordinates = new LatLng(getLat(), getLng());
            return myCoordinates.toString();
        }
        return new String("Undefined");
    }


    public LatLng getCurrentLocation() {
        locationManager = (LocationManager) PettifyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*
             TODO: Consider calling
                ActivityCompat#requestPermissions
             here to request the missing permissions, and then overriding
               public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                      int[] grantResults)
             to handle the case where the user grants the permission. See the documentation
             for ActivityCompat#requestPermissions for more details.
            */
            Log.d("location", "Client does not have location/network permmisons please active permissions");
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            LatLng myCoordinates = new LatLng(lat, lng);
            return myCoordinates;
        }
        return new LatLng(0.0, 0.0);
    }


    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
