package com.pettify.model.user;

import android.os.AsyncTask;

import com.pettify.model.AppLocalDb;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public static final UserModel instance = new UserModel();

    private UserModel() {
    }
    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface EmptyListener {
        void onComplete();
    }

//    public User getCurrentUser() {
//        return UserFireBase.getCurrentUser();
//    }

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


//
//    public boolean isUserLoggedIn() {
//        return this.getCurrentUser() != null;
//    }

//    public void register(User user, String password, Listener<Boolean> listener) {
//        UserFireBase.register(user, password, listener);
//    }


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

//    public void login(String email, String password, Listener<Boolean> listener) {
//        UserFireBase.login(email, password, listener);
//    }
//
//    public void logout() {
//        UserFireBase.logout();
//    }

}
