package com.pettify.ui.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.pettify.R;
import com.pettify.model.listeners.Listener;
import com.pettify.model.user.User;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    TextView tvProgressLabel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View registerView = inflater.inflate(R.layout.fragment_register, container, false);
        View registerButton = registerView.findViewById(R.id.register_button);
        SeekBar seekBar =  registerView.findViewById(R.id.radius_eekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int progress = seekBar.getProgress();
        tvProgressLabel =  registerView.findViewById(R.id.accountTitle_textView);
        tvProgressLabel.setText(String.valueOf(progress));
        registerButton.setOnClickListener(buttonView -> {
            TextView email = registerView.findViewById(R.id.register_user_email);
            TextView password = registerView.findViewById(R.id.register_user_password);
            TextView name = registerView.findViewById(R.id.register_user_name);
            TextView phone = registerView.findViewById(R.id.register_user_phoneNumber);
            User user = new User(name.getText().toString(), email.getText().toString());
            user.setPhoneNumber(phone.getText().toString());
            int currentProgress = seekBar.getProgress();
            user.setRadius(currentProgress);
            if (fileWasNotProvided(email, password, name, phone)) {
                registerView.findViewById(R.id.register_error_msg).setVisibility(View.VISIBLE);
            } else {
                registerView.findViewById(R.id.register_error_msg).setVisibility(View.INVISIBLE);
                authViewModel.registerUser(user, password.getText().toString(), new Listener<Task<AuthResult>>() {
                    @Override
                    public void onComplete(Task<AuthResult> data) {
                        if (data.isSuccessful()) {
                            Navigation.findNavController(registerView).navigate(R.id.action_nav_register_to_nav_home);
                        } else {
                            TextView error = registerView.findViewById(R.id.register_error_msg);
                            error.setText(data.getException().getMessage());
                            error.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        return registerView;
    }

    private boolean fileWasNotProvided(TextView email, TextView password, TextView name, TextView phone) {
        return TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())
                       || TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(phone.getText());
    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {


        public int radiusProgress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            tvProgressLabel.setText(String.valueOf(progress));
            radiusProgress=progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
           // tvProgressLabel.setText(radiusProgress);
        }
    };
}