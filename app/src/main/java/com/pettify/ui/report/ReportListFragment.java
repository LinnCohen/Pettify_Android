package com.pettify.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.pettify.R;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReportListFragment extends Fragment {

    private ReportListViewModel reportListViewModel;
    MyAdapter adapter;
    TextView reportDescription;
    ListView list;
    ReportListViewModel reportViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportViewModel =
                new ViewModelProvider(this).get(ReportListViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        list = view.findViewById(R.id.reportslist_list);

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        reportListViewModel.getReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reports) {
                adapter.notifyDataSetChanged();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = reportListViewModel.getReports().getValue().get(i).getId();
                ReportListFragmentDirections.ActionReportslistListToViewReport direc = ReportListFragmentDirections.actionReportslistListToViewReport(id);
                Navigation.findNavController(view).navigate(direc);
            }
        });

        reloadData();
        return view;
    }

    void reloadData(){
        reportViewModel.refreshAllReports(() -> { });
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

            TextView tv = view.findViewById(R.id.listrow_report_item);
            ImageView iv = view.findViewById(R.id.listrow_report_image);
            Report report = reportListViewModel.getReports().getValue().get(i);
            if (report.getImage_url() != null) {
                Picasso.get().load(report.getImage_url()).placeholder(R.drawable.images).into(iv);
            }
            tv.setText(report.getTitle()+", " + report.getAddress());
            return view;
        }
    }
}