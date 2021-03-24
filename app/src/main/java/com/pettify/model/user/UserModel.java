package com.pettify.model.user;


import android.content.pm.LabeledIntent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

import java.util.List;

public class UserModel {

    public final static UserModel instance = new UserModel();
    UserModelFireBase userModelFireBase = UserModelFireBase.instance;
    UserModelSql userModelSql = UserModelSql.instance;

    private UserModel(){}


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

    public void registerUser(User user, String password, final Listener<Boolean> listener) {
        userModelFireBase.register(user, password, listener);
    }

    public void login(String email, String password, final Listener<Boolean> listener) {
        userModelFireBase.login(email, password, listener);
    }

    public void logout() {
        userModelFireBase.logout();
    }

    public User getCurrentUser() {
        return userModelFireBase.getCurrentUser();
    }
}
