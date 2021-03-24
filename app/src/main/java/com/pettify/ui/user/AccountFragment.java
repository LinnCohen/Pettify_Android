package com.pettify.ui.user;

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
import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;
import com.pettify.ui.report.ReportListViewModel;
import com.pettify.ui.report.ViewReportFragmentArgs;

public class AccountFragment extends Fragment {

    EditText name;
    EditText email;
    EditText phone;
    SeekBar radius;
    AuthViewModel authViewModel;

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

        final User currentUser = authViewModel.getCurrentUser();

        name.setText(currentUser.getName());
        email.setText(currentUser.getEmail());
        phone.setText(currentUser.getPhoneNumber());
        Log.d("TAG", "fidsfkj");
        int radiusProgress = (int)(currentUser.getRadius());
        Log.d("TAG", String.valueOf(radiusProgress));
        Log.d("TAG", String.valueOf(currentUser.getRadius()));
        radius.setProgress(radiusProgress);


        return view;
    }
}