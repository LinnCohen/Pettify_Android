package com.pettify.model.report;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SnapshotMetadata;
import com.pettify.model.Model;
import com.pettify.model.PettifyApplication;

import java.util.List;
import java.util.Map;

public class ReportModel implements Model {

    public final static ReportModel instance = new ReportModel();
    public static final String REPORT_LAST_UPDATED = "reportLastUpdated";
    ReportModelFireBase reportModelFireBase = ReportModelFireBase.instance;
    ReportModelSql reportModelSql = ReportModelSql.instance;

    private ReportModel(){}

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

}
