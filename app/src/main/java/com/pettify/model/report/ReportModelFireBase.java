package com.pettify.model.report;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pettify.model.Model;

import java.io.ByteArrayOutputStream;
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

    public void getAllReports(long lastUpdated, ReportModel.Listener<QuerySnapshot> listener) {
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

    public void addReport(Report report, ReportModel.EmptyListener listener) {
        db.collection(REPORTS_COLLECTION)
                .add(report.toMap())
                .addOnSuccessListener(documentReference -> listener.onComplete())
                .addOnFailureListener(e -> {
                    listener.onComplete(); });
    }

    public void updateReport(Report report, ReportModel.EmptyListener listener) {
        addReport(report, listener);
    }

    public void deleteReport(String reportId, ReportModel.EmptyListener listener) {
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

    public void uploadImage(Bitmap imageBmp, String name, final Model.UploadImageListener listener){
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
