package com.pettify.ui.auth;

import androidx.lifecycle.ViewModel;

import com.pettify.model.Model;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

public class AuthViewModel extends ViewModel {

    private UserModel userModel;
    public AuthViewModel() {
        userModel = UserModel.instance;
    }

    public void registerUser(User user, String password, Model.Listener<Boolean> listener) {
        userModel.registerUser(user, password, listener);
    }

    public void loginUser(String email, String password, Model.Listener<Boolean> listener) {
        userModel.login(email, password, listener);
    }

}