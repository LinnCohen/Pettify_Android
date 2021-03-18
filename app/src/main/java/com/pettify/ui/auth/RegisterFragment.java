package com.pettify.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.pettify.R;
import com.pettify.model.user.User;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        View registerButton = root.findViewById(R.id.register_button);
        registerButton.setOnClickListener(buttonView -> {
            TextView email = root.findViewById(R.id.register_user_email);
            TextView password = root.findViewById(R.id.register_user_password);
            TextView name = root.findViewById(R.id.register_user_name);
            User user = new User(name.getText().toString(), email.getText().toString());
            registerViewModel.registerUser(user, password.getText().toString(),
                    isCreated -> {
                        if (isCreated) {
                            NavController navController = Navigation.findNavController(root);
                            navController.navigateUp();
                            navController.navigateUp();
                        }
                    });
        });
        return root;
    }
}