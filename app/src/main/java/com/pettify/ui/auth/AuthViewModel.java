package com.pettify.ui.auth;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.pettify.model.listeners.Listener;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

public class AuthViewModel extends ViewModel {

    private UserModel userModel;

    public AuthViewModel() {
        userModel = UserModel.instance;
    }

    public void registerUser(User user, String password, Listener<Task<AuthResult>> listener) {
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


    public void onUserChange(Listener<FirebaseUser> firebaseUserListener) {
        userModel.onUserChange(firebaseUserListener);
    }

    public void getUser(String id, final Listener<User> listener) {
        userModel.getUser(id, listener);
    }
}