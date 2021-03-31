package com.pettify.ui.report;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pettify.R;
import com.pettify.model.PettifyApplication;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.report.Report;

import com.pettify.model.user.User;
import com.pettify.ui.auth.AuthViewModel;

import com.pettify.utilities.LocationUtils;
import com.pettify.utilities.SortReports;
import com.squareup.picasso.Callback;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ReportListFragment extends Fragment {

    private ReportListViewModel reportListViewModel;
    ReportListAdapter adapter;
    RecyclerView reports_list;
    List<Report> reportData = new LinkedList<>();
    AuthViewModel authViewModel;
    String currentUserId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);
        authViewModel =
                new ViewModelProvider(this).get(AuthViewModel.class);
        User user = authViewModel.getCurrentUser();
        if(user != null) {
            currentUserId = user.getId();
        }
        reports_list = view.findViewById(R.id.reportslist_list);
        reports_list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        reports_list.setLayoutManager(layoutManager);

        adapter = new ReportListAdapter();
        reports_list.setAdapter(adapter);

        adapter.setOnItemClickListener(position ->  {
            String id = reportListViewModel.getReports().getValue().get(position).getId();
            ReportListFragmentDirections.ActionReportslistListToViewReport direc = ReportListFragmentDirections.actionReportslistListToViewReport().setReportId(id);
            Navigation.findNavController(view).navigate(direc);
        });

        reportListViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            reportData = reports;
            if(reportData != null && ContextCompat.checkSelfPermission(PettifyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
                Collections.sort(reportData, new SortReports(LocationUtils.instance.getCurrentLocation()));
            adapter.notifyDataSetChanged();
        });
        reloadData();
        return view;
    }

    void reloadData() {
        reportListViewModel.refreshAllReports(() -> {});
    }

    void deleteReport(String id, EmptyListener listener) {
        reportListViewModel.deleteReport(id, listener);
    }

    static class ReportRowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
        ProgressBar pb;

        public ReportRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            description = itemView.findViewById(R.id.listrow_report_description);
            title = itemView.findViewById(R.id.listrow_report_title);
            image = itemView.findViewById(R.id.listrow_report_image);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onClick(position);
                    }
                }
            });
        }

        public void bindData(Report report) {
            description.setText(report.getDescription());
            title.setText(report.getTitle());
            pb = itemView.findViewById(R.id.list_row_progress_bar);
            pb.setVisibility(View.VISIBLE);

            if (report.getImage_url() != null && !report.getImage_url().isEmpty())
                Picasso.get().load(report.getImage_url()).into(image, new Callback() {
                    @Override
                    public void onSuccess() {
                        pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) { }
                });
            else
                image.setImageResource(R.drawable.images);
        }
    }

    interface OnItemClickListener {
        void onClick(int position);
    }

    class ReportListAdapter extends RecyclerView.Adapter<ReportRowViewHolder> {
        private OnItemClickListener listener;


        void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public ReportRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.report_list_row, parent, false);
            ReportRowViewHolder viewHolder = new ReportRowViewHolder(view, listener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReportRowViewHolder holder, int position) {
            Report report = reportData.get(position);
            Button delete_report = holder.itemView.findViewById(R.id.listrow_delete_report);
            Button edit_report = holder.itemView.findViewById(R.id.listrow_edit_report);
            String id = reportListViewModel.getReports().getValue().get(position).getId();

            delete_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteReport(id, new EmptyListener() {
                        @Override
                        public void onComplete() {
                            Log.d("TAG", "Successfully deleted report");
                            reloadData();
                        }
                    });
                }
            });

            edit_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReportListFragmentDirections.ActionReportslistListToCreateReport direction = ReportListFragmentDirections.actionReportslistListToCreateReport(id);
                    Navigation.findNavController(holder.itemView).navigate(direction);
                }
            });
            if(!isUserReporter(report.getReporterId())){
                delete_report.setVisibility(View.INVISIBLE);
                edit_report.setVisibility(View.INVISIBLE);
            } else {
                delete_report.setVisibility(View.VISIBLE);
                edit_report.setVisibility(View.VISIBLE);
            }
            holder.bindData(report);

        }

        @Override
        public int getItemCount() {
            return reportData.size();
        }

        public boolean isUserReporter(String reporterId){
            boolean test = reporterId.equals(currentUserId);
            return test;
        }
    }
}
