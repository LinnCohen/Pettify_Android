package com.pettify.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
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
            if (TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())) {
                TextView loginError = loginView.findViewById(R.id.login_error_msg);
                loginError.setText("Fill email and password!");
                loginError.setVisibility(View.VISIBLE);
            }
            else {
                authViewModel.loginUser(email.getText().toString(), password.getText().toString(),
                        isSuccess -> {
                            if (isSuccess) {
                                NavController navigation = Navigation.findNavController(loginView);
                                navigation.navigateUp();
                                navigation.navigate(R.id.nav_home);
                            } else {
                                TextView loginError = loginView.findViewById(R.id.login_error_msg);
                                loginError.setText("Failed to login");
                                loginError.setVisibility(View.VISIBLE);                            }
                        });
            }

        });

        View noAccount = loginView.findViewById(R.id.noAccount_button);
        noAccount.setOnClickListener(buttonView -> {
            Navigation.findNavController(loginView).navigate(R.id.action_nav_login_to_nav_register);
        });

        return loginView;
    }
}