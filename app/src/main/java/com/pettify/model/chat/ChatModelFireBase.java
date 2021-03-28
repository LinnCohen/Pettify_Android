//package com.pettify.model.chat;
//
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//
//import com.google.firebase.Timestamp;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QuerySnapshot;
//import com.pettify.model.listeners.EmptyListener;
//import com.pettify.model.listeners.Listener;
//import com.pettify.model.report.Report;
//import com.pettify.model.report.ReportModelFireBase;
//
//public class ChatModelFireBase {
//
//    private static final String CHATS_COLLECTION = "chats";
//    public static final ChatModelFireBase instance = new ChatModelFireBase();
//    private FirebaseFirestore db;
//
//    private ChatModelFireBase() {
//        db = FirebaseFirestore.getInstance();
//    }
//
//
//    public void getAllChats(long lastUpdated, Listener<QuerySnapshot> listener) {
//        Timestamp ts = new Timestamp(lastUpdated,0);
//        db.collection(CHATS_COLLECTION).whereGreaterThanOrEqualTo("lastUpdated",ts)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.d("TAG", "", error);
//                            listener.onComplete(null);
//                        }
//                        listener.onComplete(value);
//                    }
//                });
//    }
//
//    public void addChat(Chat chat, EmptyListener listener) {
//        db.collection(CHATS_COLLECTION)
//                .add(chat.toMap())
//                .addOnSuccessListener(documentReference -> {
//                    listener.onComplete();
//                })
//                .addOnFailureListener(e -> listener.onComplete()
//                );
//    }
//
//
//}
