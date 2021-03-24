package com.pettify.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pettify.R;
import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ReportListFragment extends Fragment {

    private ReportListViewModel reportListViewModel;
    ReportListAdapter adapter;
    RecyclerView reports_list;
    List<Report> reportData = new LinkedList<>();
    ReportListViewModel reportViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportViewModel =
                new ViewModelProvider(this).get(ReportListViewModel.class);
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
        swipeRefresh.setOnRefreshListener(() -> reportViewModel.refreshAllReports(new EmptyListener() {
            @Override
            public void onComplete() {
                swipeRefresh.setRefreshing(false);
            }
        }));

        reportListViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            reportData = reports;
            adapter.notifyDataSetChanged();
        });
        return view;
    }

    static class ReportRowViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        ImageView image;

        public ReportRowViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            description = itemView.findViewById(R.id.listrow_report_item);
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

        public void bind(Report report) {
            description.setText(report.getDescription());

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
            holder.bind(report);
        }

        @Override
        public int getItemCount() {
            return reportData.size();
        }
    }
}