package com.pettify.model.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class UserModelFireBase {
    private static final String USERS_COLLECTION = "users";
    public static final UserModelFireBase instance = new UserModelFireBase();
    private  FirebaseFirestore db;
    private  FirebaseAuth auth;

    private UserModelFireBase() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void onUserChange(Listener<FirebaseUser> listener) {
        auth.addAuthStateListener(firebaseAuth -> listener.onComplete(firebaseAuth.getCurrentUser()));
    }

    public void getUser(String id, Listener<User> listener) {
        db.collection(USERS_COLLECTION)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    User user = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null) {
                            user = doc.toObject(User.class);
                        }
                        listener.onComplete(user);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    public User getCurrentUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser == null ? null :
                new User(
                        firebaseUser.getDisplayName(),
                        firebaseUser.getEmail(),
                        firebaseUser.getUid());
    }

    public void register(final User user, String password, final Listener<Task<AuthResult>> listener) {
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userUid=task.getResult().getUser().getUid();
                            user.setId(userUid);
                            //TODO - async
                            db.collection(USERS_COLLECTION).document(userUid).set(user);
                            updateUserProfile(user, new Listener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {
                                    if (data) {
                                        listener.onComplete(task);
                                    }
                                }
                            });
                        } else {
                            Log.w("TAG", "Failed to register user", task.getException());
                            if (listener != null) {
                                listener.onComplete(task);
                            }
                        }
                    }
                });
    }

    public void login(String email, String password, final Listener<Boolean> listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (listener != null) {
                            listener.onComplete(true);
                        }
                    } else {
                        Log.i("TAG", "Failed to login user", task.getException());
                        if (listener != null) {
                            listener.onComplete(false);
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }

    private  void updateUserProfile(User user, final Listener<Boolean> listener) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(user.getName()).build();

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> listener.onComplete(task.isSuccessful()));
    }


}
