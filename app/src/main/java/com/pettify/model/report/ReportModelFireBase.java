package com.pettify.model.report;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ReportModelFireBase {
    private static final String REPORTS_COLLECTION = "reports";
    public static final ReportModelFireBase instance = new ReportModelFireBase();
    private  FirebaseFirestore db;

    private ReportModelFireBase() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllReports(long lastUpdated, ReportModel.Listener<List<Report>> listener) {
        Timestamp ts = new Timestamp(lastUpdated,0);
        List<Report> reports = new LinkedList<>();
        db.collection(REPORTS_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated",ts)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Report report = new Report();
                            report.fromMap(document.getData());
                            reports.add(report);
                        }
                        listener.onComplete(reports);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void addReport(Report report, ReportModel.EmptyListener listener) {
        db.collection(REPORTS_COLLECTION)
                .document(report.getId())
                .set(report.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "report with id:" + report.getId() + " was created");
                        listener.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "report with id:" + report.getId() + " failed to be created");
                listener.onComplete();
            }}
        );
    }

    public void updateReport(Report report, ReportModel.EmptyListener listener) {
        addReport(report, listener);
    }
}
