package com.pettify.model.user;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pettify.model.AppLocalDb;

import java.util.List;

public class UserModel {

    public final static UserModel instance = new UserModel();
    UserModelFireBase userModelFireBase = UserModelFireBase.instance;
    UserModelSql userModelSql = UserModelSql.instance;

    private UserModel(){}

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface EmptyListener {
        void onComplete();
    }

    MutableLiveData<List<User>> userList = new MutableLiveData<>();
    public MutableLiveData<List<User>> getAllUsers() {
        return userList;
    }

    public void refreshAllUsers(EmptyListener listener) {
        userModelFireBase.getAllUsers(data -> {
            userList.setValue(data);
            listener.onComplete();
        });
    }

    public void getUser(String id, final Listener<User> listener) {
            userModelFireBase.getUser(id, listener);
    }

    public void addUser(final User user, final EmptyListener listener) {
        userModelFireBase.addUser(user, listener);
    }

    public void updateUser(final User user, final EmptyListener listener) {
        userModelFireBase.updateUser(user, listener);
    }

    public void deleteUser(String id, final EmptyListener listener) {
        userModelFireBase.deleteUser(id, listener);
    }

}
