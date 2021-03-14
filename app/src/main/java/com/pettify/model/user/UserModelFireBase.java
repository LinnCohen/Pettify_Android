package com.pettify.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class UserModelFireBase {
    private static final String USERS_COLLECTION = "users";
    public static final UserModelFireBase instance = new UserModelFireBase();

    private UserModelFireBase() {
    }

    public void getAllUsers(UserModelSql.Listener<List<User>> listener) {
        List<User> users = new LinkedList<>();
        listener.onComplete(users);
    }

    public void addUser(User user, UserModelSql.EmptyListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("name", user.name);
        newUser.put("email", user.email);

        // Add a new document with a generated ID
        db.collection(USERS_COLLECTION)
                .add(newUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
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
