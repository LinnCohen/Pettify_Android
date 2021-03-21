package com.pettify.model.report;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentChange;
import com.pettify.model.Model;
import com.pettify.model.PettifyApplication;


import java.util.List;

public class ReportModel implements Model {

    public final static ReportModel instance = new ReportModel();
    public static final String REPORT_LAST_UPDATED = "reportLastUpdated";
    ReportModelFireBase reportModelFireBase = ReportModelFireBase.instance;
    ReportModelSql reportModelSql = ReportModelSql.instance;

    private ReportModel() {
    }

    LiveData<List<Report>> reportsList;

    public LiveData<List<Report>> getAllReports() {
        if (reportsList == null) {
            reportsList = reportModelSql.getAllReports();
        }
        return reportsList;
    }

    public void refreshAllReports(EmptyListener listener) {
        //1. get local last update date
        SharedPreferences sharedPreferences = PettifyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sharedPreferences.getLong(REPORT_LAST_UPDATED, 0);

        //2. get all updated records from fire base from the last update date
        reportModelFireBase.getAllReports(lastUpdated, querySnapshot -> {
            //3. insert the new updates and addition to the local db and delete from local db removed reports.
            long newLastUpdated = 0L;
            for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                Report report = new Report();
                report.fromMap(documentChange.getDocument().getData());
                switch (documentChange.getType()) {
                    case ADDED:
                    case MODIFIED:
                        reportModelSql.addReport(report, null);
                        if (report.getLastUpdated() > newLastUpdated) {
                            newLastUpdated = report.getLastUpdated();
                        }
                        break;
                    case REMOVED:
                        reportModelSql.deleteReport(report, null);
                        break;
                }
            }

            //4. update the local last update date
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(REPORT_LAST_UPDATED, newLastUpdated);
            editor.commit();
            //5. return the updated data to the listeners - all the data
            if (listener != null) {
                listener.onComplete();
            }
        });
    }

    public void addReport(final Report report, final ReportModel.EmptyListener listener) {
        reportModelFireBase.addReport(report, listener);
    }

    public void updateReport(final Report report, final EmptyListener listener) {
        reportModelFireBase.updateReport(report, listener);
    }

    public void uploadImage(Bitmap imageBmp, String name, final Model.UploadImageListener listener) {
        reportModelFireBase.uploadImage(imageBmp, name, listener);
    }

    public LatLng getLocation() {
//            if (ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return new LatLng(0, 0);
//            }

        LocationManager locationManager = (LocationManager) PettifyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return new LatLng(0, 0);
        }
        Location location = locationManager.getLastKnownLocation(provider);
            return new LatLng(location.getLatitude() , location.getLongitude());
        }

}
