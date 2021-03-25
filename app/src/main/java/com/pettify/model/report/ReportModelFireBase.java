package com.pettify.model.report;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ReportModelFireBase {
    private static final String REPORTS_COLLECTION = "reports";
    public static final ReportModelFireBase instance = new ReportModelFireBase();
    private  FirebaseFirestore db;

    private ReportModelFireBase() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllReports(long lastUpdated, Listener<QuerySnapshot> listener) {
        Timestamp ts = new Timestamp(lastUpdated,0);
        List<Report> reports = new LinkedList<>();
        db.collection(REPORTS_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated",ts)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onComplete(task.getResult());
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void addReport(Report report, EmptyListener listener) {
        db.collection(REPORTS_COLLECTION)
                .add(report.toMap())
                .addOnSuccessListener(documentReference -> listener.onComplete())
                .addOnFailureListener(e -> listener.onComplete()
                );
    }

    public void getReport(String id, Listener listener) {
        db.collection(REPORTS_COLLECTION)
                .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Report report = null;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        if (doc.exists()) {
                            report = new Report();
                            report.fromMap(doc.getData());
                        }
                    }
                }
                listener.onComplete(report);
            }
        });
    }

    public void updateReport(Report report, EmptyListener listener) {
        Map<String, Object> reportMap = report.toMap();
        db.collection(REPORTS_COLLECTION).document(report.getId()).set(reportMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) listener.onComplete();
            }
        });
    }

    public void deleteReport(String reportId, EmptyListener listener) {
        db.collection(REPORTS_COLLECTION).document(reportId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (listener != null) {
                        listener.onComplete();
                    }
                }
                else {
                    Log.w("TAG", "Failed to delete report", task.getException());
                }
            }
        });
    }

    public void uploadImage(Bitmap imageBmp, String name, final Listener<String> listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null)).addOnSuccessListener(taskSnapshot -> imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Uri downloadUrl = uri;
            listener.onComplete(downloadUrl.toString());
        }));
    }
}
