package com.pettify.model.message;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentChange;
import com.pettify.model.PettifyApplication;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

import java.util.List;

public class MessageModel {
    public final static MessageModel instance = new MessageModel();
    public static final String CHAT_LAST_UPDATED = "chatLastUpdated";
    private MessageModelFireBase messageModelFireBase = MessageModelFireBase.instance;
    private MessageModelSql messageModelSql = MessageModelSql.instance;

    public MessageModel() {
    }

    private LiveData<List<Message>> chat;

    public LiveData<List<Message>> getAllMessages() {
        if (chat == null) {
            chat = messageModelSql.getAllMessages();
        }
        return chat;
    }


    public void refreshAllMessages(EmptyListener listener) {
        //1. get local last update date
        Log.d("TAG","we on refreshh");
        SharedPreferences sharedPreferences = PettifyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        long lastUpdated = sharedPreferences.getLong(CHAT_LAST_UPDATED, 0);

        //2. get all updated records from fire base from the last update date
        messageModelFireBase.getAllMessages(lastUpdated, querySnapshot -> {
            //3. insert the new updates and addition to the local db and delete from local db removed reports.
            long newLastUpdated = 0L;
            for (DocumentChange documentChange : querySnapshot.getDocumentChanges()) {
                Message message = new Message();
                message.fromMap(documentChange.getDocument().getData());
                Log.d("TAG", documentChange.getType().toString());
                switch (documentChange.getType()) {
                    case ADDED:
                    case MODIFIED:
                        Log.d("TAG","We here :)");
                        message.setId(documentChange.getDocument().getId());
                        messageModelSql.addMessage(message, null);
                        if (message.getLastUpdated() > newLastUpdated) {
                            newLastUpdated = message.getLastUpdated();
                        }
                        break;

                }
            }

            //4. update the local last update date
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong(CHAT_LAST_UPDATED, newLastUpdated);
            editor.commit();
            //5. return the updated data to the listeners - all the data
            if (listener != null) {
                listener.onComplete();
            }
        });
    }


    public void addMessage(final Message message, final EmptyListener listener) {
        messageModelFireBase.addMessage(message, listener);
    }

    public void getMessage(String id, Listener listener) {
        messageModelFireBase.getMessage(id, listener);
    }
}
