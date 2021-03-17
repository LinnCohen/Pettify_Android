package com.pettify.ui.report;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import java.util.List;

public class ReportListViewModel extends ViewModel {
    private LiveData<List<Report>> reports = ReportModel.instance.getAllReports();

    public LiveData<List<Report>> getReports() {
        return reports;
    }

}
