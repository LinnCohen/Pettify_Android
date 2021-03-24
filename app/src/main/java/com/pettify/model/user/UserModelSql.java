package com.pettify.model.user;

import android.os.AsyncTask;

import com.pettify.model.AppLocalDb;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

import java.util.List;

public class UserModelSql {
    public static final UserModelSql instance = new UserModelSql();

    private UserModelSql() {
    }


    public void getAllUsers(final Listener<List<User>> listener) {
        class MyAsyncTask extends AsyncTask{
            List<User> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalDb.db.userDao().getAll();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }


    public void addUser(final User user, final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.userDao().insertAll(user);
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
