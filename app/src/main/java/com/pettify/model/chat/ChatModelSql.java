package com.pettify.model.chat;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pettify.model.AppLocalDb;
import com.pettify.model.listeners.EmptyListener;

import java.util.List;


public class ChatModelSql {

    public static final ChatModelSql instance = new ChatModelSql();

    public ChatModelSql() {
    }

    public LiveData<List<Chat>> getAllChats() {
        return AppLocalDb.db.chatDao().getAll();
    }

    public void addChat(final Chat chat, final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.chatDao().insertAll(chat);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void deleteChat(Chat chat,  final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.chatDao().delete(chat);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}
