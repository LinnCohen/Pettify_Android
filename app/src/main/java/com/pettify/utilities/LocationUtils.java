package com.pettify.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;
import com.pettify.model.PettifyApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class LocationUtils implements LocationListener {
    public final static LocationUtils instance = new LocationUtils();
    private  LocationManager locationManager;
    private double lat = 0.0;
    private double lng = 0.0;

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
            setLat(location.getLatitude());
            setLng(location.getLongitude());
        }

    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
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
        setLat(location.getLatitude());
        setLng(location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
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
            setLat(location.getLatitude());
            setLng(location.getLongitude());
            return new LatLng(getLat(), getLng());

        }
        return new LatLng(32.0851703, 35.139172); // retrun default location if cant get current location
    }

    public String getAddressName(Context context, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        List<Address> addressList = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            return "no valid address found";
        }
        return addressList.get(0).getAddressLine(0);
    }


}
