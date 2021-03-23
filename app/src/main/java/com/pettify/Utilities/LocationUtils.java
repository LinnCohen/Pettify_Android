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


    private LocationUtils() {
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    public String getCurrentLocationAsString() {
        LocationManager locationManager = (LocationManager) PettifyApplication.context.getSystemService(Context.LOCATION_SERVICE);
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
        if(location !=null) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            return myCoordinates.toString();
        }
        return new String("Undefind");
    }


}
