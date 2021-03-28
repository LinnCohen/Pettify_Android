//package com.pettify.model.chat;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.util.Log;
//
//import androidx.lifecycle.LiveData;
//
//import com.google.firebase.firestore.DocumentChange;
//import com.pettify.model.PettifyApplication;
//import com.pettify.model.listeners.EmptyListener;
//import com.pettify.model.report.Report;
//import com.pettify.model.report.ReportModel;
//import com.pettify.model.report.ReportModelFireBase;
//import com.pettify.model.report.ReportModelSql;
//
//import java.util.List;
//
//public class ChatModel {
//    public final static ChatModel instance = new ChatModel();
//    public static final String REPORT_LAST_UPDATED = "reportLastUpdated";
//    ChatModelFireBase chatModelFireBase = ChatModelFireBase.instance;
//    ChatModelSql chatModelSql = ChatModelSql.instance;
//
//
//    public ChatModel() {
//    }
//
//    LiveData<List<Chat>> chatsList;
//
//    public LiveData<List<Chat>> getAllChats() {
//        if (chatsList == null) {
//            chatsList = chatModelSql.getAllChats();
//        }
//        return chatsList;
//    }
//
//
//    public void refreshAllChats(EmptyListener listener) {
//        //1. get local last update date
//        SharedPreferences sharedPreferences = PettifyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
//        long lastUpdated = sharedPreferences.getLong(REPORT_LAST_UPDATED, 0);
//
//        //2. get all updated records from fire base from the last update date
//        chatModelFireBase.getAllChats(lastUpdated, querySnapshot -> {
//            //3. insert the new updates and addition to the local db and delete from local db removed reports.
//            long newLastUpdated = 0L;
//            for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
//                Chat chat = new Chat();
//                chat.fromMap(documentChange.getDocument().getData());
//                Log.d("TAG", documentChange.getType().toString());
//                switch (documentChange.getType()) {
//                    case ADDED:
//                    case MODIFIED:
//                        chat.setId(documentChange.getDocument().getId());
//                        chatModelSql.addChat(chat, null);
//                        if (chat.getLastUpdated() > newLastUpdated) {
//                            newLastUpdated = chat.getLastUpdated();
//                        }
//                        break;
//
//                }
//            }
//
//            //4. update the local last update date
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putLong(REPORT_LAST_UPDATED, newLastUpdated);
//            editor.commit();
//            //5. return the updated data to the listeners - all the data
//            if (listener != null) {
//                listener.onComplete();
//            }
//        });
//    }
//
//
//    public void addChat(final Chat chat, final EmptyListener listener) {
//        chatModelFireBase.addChat(chat, listener);
//    }
//}
