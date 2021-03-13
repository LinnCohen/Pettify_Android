package com.pettify.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserFireBase {
//
//    public static User getCurrentUser() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        return firebaseUser == null ? null : factory(firebaseUser);
//    }
//
//    public static void register(final User user, String password, final UserModel.Listener<Boolean> listener) {
//        final FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.createUserWithEmailAndPassword(user.email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            updateUserProfile(user, listener);
//                        } else {
//                            Log.w("TAG", "Failed to register user", task.getException());
//                            if (listener != null) {
//                                listener.onComplete(false);
//                            }
//                        }
//                    }
//                });
//    }
//
//    public static void login(String email, String password, final UserModel.Listener<Boolean> listener) {
//        final FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            if (listener != null) {
//                                listener.onComplete(true);
//                            }
//                        } else {
//                            Log.i("TAG", "Failed to login user", task.getException());
//                            if (listener != null) {
//                                listener.onComplete(false);
//                            }
//                        }
//                    }
//                });
//    }
//
//    public static void logout() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        auth.signOut();
//    }
//
//    private static void updateUserProfile(User user, final UserModel.Listener<Boolean> listener) {
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user.getName()).build();
//
//        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                listener.onComplete(task.isSuccessful());
//            }
//        });
//    }
//
//    private static User factory(FirebaseUser firUser) {
//        return new User(
//                firUser.getUid(),
//                firUser.getDisplayName(),
//                firUser.getEmail()
//        );
//    }

}
