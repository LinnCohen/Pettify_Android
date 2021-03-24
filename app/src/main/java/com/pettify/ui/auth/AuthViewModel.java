package com.pettify.ui.auth;

import androidx.lifecycle.ViewModel;

import com.pettify.model.listeners.Listener;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

public class AuthViewModel extends ViewModel {

    private UserModel userModel;
    public AuthViewModel() {
        userModel = UserModel.instance;
    }

    public void registerUser(User user, String password, Listener<Boolean> listener) {
        userModel.registerUser(user, password, listener);
    }

    public void loginUser(String email, String password, Listener<Boolean> listener) {
        userModel.login(email, password, listener);
    }

    public User getCurrentUser(){
        return userModel.getCurrentUser();
    }


    public void logout(){
        userModel.logout();
    }
}