package com.pettify.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.fragment.app.Fragment;

import com.pettify.R;
import com.pettify.model.Model;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

public class ViewReportFragment extends Fragment {
    Report report;
    TextView report_title;
    TextView report_description;
    TextView report_posted_on;
    TextView report_location;
    ImageView report_image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_report, container, false);
        report_description = view.findViewById(R.id.report_description);
        report_image = view.findViewById(R.id.report_image);
        report_location = view.findViewById(R.id.report_address);
        report_posted_on = view.findViewById(R.id.report_posted_at);
        report_title = view.findViewById(R.id.report_title);

        final String reportId = ViewReportFragmentArgs.fromBundle(getArguments()).getReportId();
        ReportModel.instance.getReport(reportId, new Listener<Report>() {
            @Override
            public void onComplete(Report data) {
                report = data;
                report_title.setText(report.getTitle());
                report_description.setText(report.getDescription());
                report_location.setText(report.getAddress());
                if (data.getImage_url() != null){
                    Picasso.get().load(data.getImage_url()).placeholder(R.drawable.images).into(report_image);
                }
            }
        });
        return view;
    }
}
