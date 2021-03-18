package com.pettify.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.user.User;

public class LoginFragment extends Fragment {

    private AuthViewModel authViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        View loginButton = root.findViewById(R.id.login_but);
        loginButton.setOnClickListener(buttonView -> {
            TextView email = root.findViewById(R.id.login_email);
            TextView password = root.findViewById(R.id.login_password);
            authViewModel.loginUser(email.getText().toString(), password.getText().toString(),
                    isSuccess -> {
                        if (isSuccess) {
                            NavController navController = Navigation.findNavController(root);
                            navController.navigateUp();
                            navController.navigateUp();
                        } else {
                            root.findViewById(R.id.login_error_msg).setVisibility(View.VISIBLE);
                        }
                    });
        });
        return root;
    }
}