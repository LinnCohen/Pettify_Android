package com.pettify.model.message;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

public class MessageModelFireBase {

    private static final String MESSAGES_COLLECTION = "messages";
    public static final MessageModelFireBase instance = new MessageModelFireBase();
    private FirebaseFirestore db;

    private MessageModelFireBase() {
        db = FirebaseFirestore.getInstance();
    }


    public void getAllMessages(long lastUpdated, Listener<QuerySnapshot> listener) {
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection(MESSAGES_COLLECTION)
                .whereGreaterThanOrEqualTo("lastUpdated",ts)   // ask mayy
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("TAG", "", error);
                            listener.onComplete(null);
                        }
                        listener.onComplete(value);
                    }
                });
    }

    public void addMessage(Message message, EmptyListener listener) {
        db.collection(MESSAGES_COLLECTION)
                .add(message.toMap())
                .addOnSuccessListener(documentReference -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> listener.onComplete()
                );
    }

    public void getMessage(String id, Listener listener) {
        db.collection(MESSAGES_COLLECTION)
                .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Message message = null;
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        if (doc.exists()) {
                            message = new Message();
                            message.fromMap(doc.getData());
                        }
                    }
                }
                listener.onComplete(message);
            }
        });
    }
}
