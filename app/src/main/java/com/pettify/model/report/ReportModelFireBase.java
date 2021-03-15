package com.pettify.model.report;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ReportModelFireBase {
    private static final String REPORTS_COLLECTION = "reports";
    public static final ReportModelFireBase instance = new ReportModelFireBase();

    private ReportModelFireBase() {
    }

    public void getAllReports(ReportModelSql.Listener<List<Report>> listener) {
        List<Report> reports = new LinkedList<>();
        listener.onComplete(reports);
    }

    public void addReport(Report report, ReportModelSql.EmptyListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newReport = new HashMap<>();
        newReport.put("description", report.description);

        // Add a new document with a generated ID
        db.collection(REPORTS_COLLECTION)
                .add(newReport)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
