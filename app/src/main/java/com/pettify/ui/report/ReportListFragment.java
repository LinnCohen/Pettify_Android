package com.pettify.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModelSql;
import com.pettify.model.report.ReportModel;
import com.pettify.model.report.ReportModelFireBase;

import java.util.LinkedList;
import java.util.List;

public class ReportListFragment extends Fragment {

    List<Report> reportList = new LinkedList<Report>();
    ProgressBar pb;
    Button addBtn;
    MyAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);

        ListView list = view.findViewById(R.id.reportslist_list);
        pb = view.findViewById(R.id.reportslist_progress);
        addBtn = view.findViewById(R.id.reportslist_add_btn);
        pb.setVisibility(View.INVISIBLE);

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        addBtn.setOnClickListener(view1 -> addReport());
        reloadData();
        return view;
    }

    private void addReport() {
        addBtn.setEnabled(false);
        int id = reportList.size();
        Report report = new Report();
        report.setId(""+id);
        report.setDescription("description of report #" + id);
        pb.setVisibility(View.VISIBLE);
//        ReportModelSql.instance.addReport(report, () -> reloadData());
        ReportModelFireBase.instance.addReport(report, () -> reloadData());
    }

    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        addBtn.setEnabled(false);
//        ReportModel.instance.getAllReports(data -> {
//            reportList = data;
//            for (Report report : data) {
//                Log.d("TAG","report id: " + report.getId());
//            }
//            pb.setVisibility(View.INVISIBLE);
//            addBtn.setEnabled(true);
//            adapter.notifyDataSetChanged();
//        });
        ReportModel.instance.getAllReports(data -> {
            reportList = data;
            for (Report report : data) {
                Log.d("TAG","report id: " + report.getId());
            }
            pb.setVisibility(View.INVISIBLE);
            addBtn.setEnabled(true);
            adapter.notifyDataSetChanged();
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return reportList.size();
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
            Report report = reportList.get(i);
            tv.setText(report.getId() + " " + report.getDescription());
            return view;
        }
    }
}