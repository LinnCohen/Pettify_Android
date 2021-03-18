package com.pettify.model.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class UserModelFireBase {
    private static final String USERS_COLLECTION = "users";
    public static final UserModelFireBase instance = new UserModelFireBase();
    private  FirebaseFirestore db;

    private UserModelFireBase() {
        db = FirebaseFirestore.getInstance();
    }

    public void getAllUsers(UserModel.Listener<List<User>> listener) {
        List<User> users = new LinkedList<>();
        db.collection(USERS_COLLECTION)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            users.add(document.toObject(User.class));
                        }
                        listener.onComplete(users);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    public void addUser(User user, UserModel.EmptyListener listener) {
        db.collection(USERS_COLLECTION)
                .document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "user with id:" + user.getId() + " was created");
                        listener.onComplete();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "user with id:" + user.getId() + " failed to be created");
                        listener.onComplete();
                    }}
                    );
    }

    public void updateUser(User user, UserModel.EmptyListener listener) {
        addUser(user, listener);
    }

    public void getUser(String id, UserModel.Listener<User> listener) {
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

    public void deleteUser(String id, UserModel.EmptyListener listener) {
        db.collection(USERS_COLLECTION)
                .document(id)
                .delete()
                .addOnCompleteListener(task -> {
                   listener.onComplete();
                });
    }
//
//    public static User getCurrentUser() {
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        return firebaseUser == null ? null : factory(firebaseUser);
//    }
//
    public void register(final User user, String password, final UserModel.Listener<Boolean> listener) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onComplete(true);
                        } else {
                            Log.w("TAG", "Failed to register user", task.getException());
                            if (listener != null) {
                                listener.onComplete(false);
                            }
                        }
                    }
                });
    }

    public static void login(String email, String password, final UserModel.Listener<Boolean> listener) {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
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

}
