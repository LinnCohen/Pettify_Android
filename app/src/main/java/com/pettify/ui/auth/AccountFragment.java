package com.pettify.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;

import com.pettify.R;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;
import com.pettify.ui.report.ReportListViewModel;
import com.pettify.ui.report.ViewReportFragmentArgs;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment {

    EditText name;
    EditText email;
    EditText phone;
    EditText radiusVal;
    SeekBar radius;
    AuthViewModel authViewModel;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        name = view.findViewById(R.id.name_editText);
        email = view.findViewById(R.id.editTextTextEmailAddress);
        phone = view.findViewById(R.id.phone_editText);
        radius = view.findViewById(R.id.radius_eekBar);
        radiusVal = view.findViewById(R.id.radius_editText);

        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        radius.setEnabled(false);
        radiusVal.setEnabled(false);

        final User currentUser = authViewModel.getCurrentUser();

        name.setText(currentUser.getName());
        email.setText(currentUser.getEmail());
        final String userId = currentUser.getId();

        authViewModel.getUser(userId, new Listener<User>() {
            @Override
            public void onComplete(User data) {
                user = data;
                phone.setText(user.getPhoneNumber());
                int radiusProgress = (int)(user.getRadius());
                radius.setProgress(radiusProgress);
                radiusVal.setText((String.valueOf(radiusProgress)));
            }
        });

        return view;
    }
}