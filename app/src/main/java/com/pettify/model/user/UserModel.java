package com.pettify.model.user;



import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.pettify.model.listeners.Listener;

public class UserModel {

    public final static UserModel instance = new UserModel();
    UserModelFireBase userModelFireBase = UserModelFireBase.instance;

    private UserModel(){}

    public void getUser(String id, final Listener<User> listener) {
            userModelFireBase.getUser(id, listener);
    }

    public void registerUser(User user, String password, final Listener<Task<AuthResult>> listener) {
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

    public void onUserChange(Listener<FirebaseUser> firebaseUserListener) {
        userModelFireBase.onUserChange(firebaseUserListener);
    }
}
