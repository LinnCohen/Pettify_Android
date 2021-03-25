package com.pettify.ui.report;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pettify.R;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.report.Report;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ReportListFragment extends Fragment {

    private static ReportListViewModel reportListViewModel;
    ReportListAdapter adapter;
    RecyclerView reports_list;
    List<Report> reportData = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_list, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        reports_list = view.findViewById(R.id.reportslist_list);
        reports_list.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        reports_list.setLayoutManager(layoutManager);

        adapter = new ReportListAdapter();
        reports_list.setAdapter(adapter);

        adapter.setOnItemClickListener(position ->  {
            String id = reportListViewModel.getReports().getValue().get(position).getId();
            ReportListFragmentDirections.ActionReportslistListToViewReport direc = ReportListFragmentDirections.actionReportslistListToViewReport(id);
            Navigation.findNavController(view).navigate(direc);
        });

        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.report_list_refresh_by_swipe);
        swipeRefresh.setOnRefreshListener(() -> reportListViewModel.refreshAllReports(new EmptyListener() {
            @Override
            public void onComplete() {
                swipeRefresh.setRefreshing(false);
            }
        }));

        reportListViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            reportData = reports;
            adapter.notifyDataSetChanged();
        });
        reloadData();
        return view;
    }

    static void reloadData() {
        reportListViewModel.refreshAllReports(() -> { });
    }

    static void deleteReport(String id, EmptyListener listener) {
        reportListViewModel.deleteReport(id, listener);
    }

    static void deleteReportLocally(Report report) {
        reportListViewModel.deleteReportLocally(report);
    }

    static class ReportRowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;
        Button edit_report;
        Button delete_report;

        public ReportRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            description = itemView.findViewById(R.id.listrow_report_description);
            title = itemView.findViewById(R.id.listrow_report_title);
            image = itemView.findViewById(R.id.listrow_report_image);
            edit_report = itemView.findViewById(R.id.listrow_edit_report);
            delete_report = itemView.findViewById(R.id.listrow_delete_report);

            edit_report.setOnClickListener(view1 -> Log.d("TAG", "Test Edit Button"));

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

            if (report.getImage_url() != null && !report.getImage_url().isEmpty())
                Picasso.get().load(report.getImage_url()).placeholder(R.drawable.images).into(image);
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.list_row, parent, false);
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
                        }
                    });
                    deleteReportLocally(report);
                }
            });

            edit_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ReportListFragmentDirections.ActionReportslistListToCreateReport direction = ReportListFragmentDirections.actionReportslistListToCreateReport(id);
                    Navigation.findNavController(holder.itemView).navigate(direction);
                }
            });
            holder.bindData(report);
        }

        @Override
        public int getItemCount() {
            return reportData.size();
        }
    }
}
