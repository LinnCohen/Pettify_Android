package com.pettify.utilities;

import com.google.android.gms.maps.model.LatLng;
import com.pettify.model.report.Report;
import com.pettify.utilities.LocationUtils;

import java.util.Comparator;

public class SortReports implements Comparator<Report> {
    LatLng currentLoc;

    public SortReports(LatLng current){
        currentLoc = LocationUtils.instance.getCurrentLocation();
    }
    @Override
    public int compare(Report r1, Report r2) {
        double lat1 = Double.parseDouble(r1.getLat());
        double lng1 = Double.parseDouble(r1.getLng());
        double lat2 = Double.parseDouble(r2.getLat());
        double lng2 = Double.parseDouble(r2.getLng());

        double distanceToPlace1 = distance(currentLoc.latitude, currentLoc.longitude, lat1, lng1);
        double distanceToPlace2 = distance(currentLoc.latitude, currentLoc.longitude, lat2, lng2);
        return (int) (distanceToPlace1 - distanceToPlace2);
    }

    public double distance(double fromLat, double fromLng, double toLat, double toLng) {
        double radius = 6378137;   // approximate Earth radius, *in meters*
        double deltaLat = toLat - fromLat;
        double deltaLon = toLng - fromLng;
        double angle = 2 * Math.asin( Math.sqrt(
                Math.pow(Math.sin(deltaLat/2), 2) +
                        Math.cos(fromLat) * Math.cos(toLat) *
                                Math.pow(Math.sin(deltaLon/2), 2) ) );
        return radius * angle;
    }

}