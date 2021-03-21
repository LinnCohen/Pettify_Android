package com.pettify.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pettify.R;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import java.util.List;

public class ReportListFragment extends Fragment {

    private ReportListViewModel reportListViewModel;
    ProgressBar pb;
    Button addBtn;
    MyAdapter adapter;
    TextView reportDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        ListView list = view.findViewById(R.id.reportslist_list);
//        pb = view.findViewById(R.id.reportslist_progress);
//        addBtn = view.findViewById(R.id.reportslist_add_btn);
//        reportDescription = view.findViewById(R.id.new_report_description);
//        pb.setVisibility(View.INVISIBLE);

        adapter = new MyAdapter();
        list.setAdapter(adapter);

//        addBtn.setOnClickListener(view1 -> addReport());
        reportListViewModel.getReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                adapter.notifyDataSetChanged();
            }
        });
        reloadData();
        return view;
    }

//    private void addReport() {
//        addBtn.setEnabled(false);
//        Log.d("TAG", String.valueOf(reportListViewModel.getReports().getValue().size()));
//        int id = reportListViewModel.getReports().getValue().size();
//        Report report = new Report();
//        report.setId(""+id);
//        report.setDescription(reportDescription.getText().toString());
////        pb.setVisibility(View.VISIBLE);
//        ReportModel.instance.addReport(report, () -> reloadData());
//    }
//
    void reloadData(){
        ReportModel.instance.refreshAllReports(() -> { });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (reportListViewModel.getReports().getValue() == null) {
                return 0;
            }
            return reportListViewModel.getReports().getValue().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null){
                view = getLayoutInflater().inflate(R.layout.list_row, null);
            }

            TextView tv = view.findViewById(R.id.listrow_test_tv);
            Report report = reportListViewModel.getReports().getValue().get(i);
            tv.setText(report.getDescription()+" " + report.getAddress());
            return view;
        }
    }
}