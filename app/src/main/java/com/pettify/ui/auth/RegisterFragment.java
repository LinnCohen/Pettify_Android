package com.pettify.ui.auth;

import android.os.Bundle;
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

import com.pettify.R;
import com.pettify.model.user.User;

public class RegisterFragment extends Fragment {

    private AuthViewModel authViewModel;
    TextView tvProgressLabel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        View registerButton = root.findViewById(R.id.register_button);
        SeekBar seekBar =  root.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        int progress = seekBar.getProgress();
        tvProgressLabel =  root.findViewById(R.id.accountTitle_textView);
        tvProgressLabel.setText("Radius: " + progress);
        registerButton.setOnClickListener(buttonView -> {
            TextView email = root.findViewById(R.id.register_user_email);
            TextView password = root.findViewById(R.id.register_user_password);
            TextView name = root.findViewById(R.id.register_user_name);
            TextView phone = root.findViewById(R.id.register_user_phoneNumber);
            User user = new User(name.getText().toString(), email.getText().toString());
            user.setPhoneNumber(phone.getText().toString());
            int currentProgress = seekBar.getProgress();
            user.setRadius(currentProgress);
            authViewModel.registerUser(user, password.getText().toString(),
                    isCreated -> {
                        if (isCreated) {
                            NavController navController = Navigation.findNavController(root);
                            navController.navigateUp();
                            navController.navigateUp();
                        }
                        else {
                            //show error
                        }
                    });
        });
        return root;
    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {


        public int radiusProgress;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            tvProgressLabel.setText("Radius: " + progress);
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