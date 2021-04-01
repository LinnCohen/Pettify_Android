package com.pettify.model.message;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pettify.model.AppLocalDb;
import com.pettify.model.listeners.EmptyListener;

import java.util.List;


public class MessageModelSql {

    public static final MessageModelSql instance = new MessageModelSql();

    public MessageModelSql() {
    }

    public LiveData<List<Message>> getAllMessages() {
        return AppLocalDb.db.messageDao().getAll();
    }

    public void addMessage(final Message message, final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.messageDao().insertAll(message);
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

    public void deleteMessage(Message message, final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.messageDao().delete(message);
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
