package com.pettify.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);
        View loginButton = loginView.findViewById(R.id.login_but);
        loginButton.setOnClickListener(buttonView -> {
            TextView email = loginView.findViewById(R.id.login_email);
            TextView password = loginView.findViewById(R.id.login_password);
            authViewModel.loginUser(email.getText().toString(), password.getText().toString(),
                    isSuccess -> {
                        if (isSuccess) {
                            Navigation.findNavController(loginView).navigate(R.id.action_nav_login_to_nav_home);
                        } else {
                            loginView.findViewById(R.id.login_error_msg).setVisibility(View.VISIBLE);
                        }
                    });
        });

        View noAccount = loginView.findViewById(R.id.noAccount_button);
        noAccount.setOnClickListener(buttonView -> {
            NavController navController = Navigation.findNavController(loginView);
            navController.navigate(R.id.nav_register);
        });

        return loginView;
    }
}