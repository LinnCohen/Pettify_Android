package com.pettify.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.List;

public class UserListViewModel extends ViewModel {
    private LiveData<List<User>> userList = UserModel.instance.getAllUsers();

    public LiveData<List<User>> getUserList() {
        return userList;
    }

}
