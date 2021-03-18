package com.pettify.ui.auth;

import androidx.lifecycle.ViewModel;

import com.pettify.model.Model;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

public class RegisterViewModel extends ViewModel {

    public RegisterViewModel() {
    }

    public void registerUser(User user, String password, Model.Listener<Boolean> listener) {
        UserModel userModel = UserModel.instance;
        userModel.registerUser(user, password, listener);
    }
}