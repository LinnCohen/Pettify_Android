package com.pettify.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.pettify.R;
import com.pettify.model.PettifyApplication;


public class CreateReportFragment extends Fragment {
    private ReportListViewModel reportListViewModel;
    private Spinner animal_type_spinner;
    private Spinner report_type_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_report, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        //animal type spinner
        animal_type_spinner = (Spinner) view.findViewById(R.id.animal_type_spinner);
        ArrayAdapter<CharSequence> animal_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.animal_types_array, android.R.layout.simple_spinner_item);

        report_type_spinner = (Spinner) view.findViewById(R.id.report_type_spinner);
        ArrayAdapter<CharSequence> report_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.report_types_array, android.R.layout.simple_spinner_item);

        animal_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animal_type_spinner.setAdapter(animal_type_adapter);
        report_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report_type_spinner.setAdapter(report_type_adapter);
        return view;
    }
}