package com.pettify.ui.report;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import java.util.List;

public class ReportListViewModel extends ViewModel {
    private LiveData<List<Report>> reports = ReportModel.instance.getAllReports();
    private ReportModel reportModel;

    public ReportListViewModel() {
        reportModel = ReportModel.instance;
    }

    public LiveData<List<Report>> getReports() {
        return reports;
    }

    public void getReport(String reportId, Listener<Report> listener) {
        reportModel.getReport(reportId, listener);
    }

    public void refreshAllReports(EmptyListener listener) {
        reportModel.refreshAllReports(listener);
    }

    public void addReport(Report report, EmptyListener listener) {
        reportModel.addReport(report, listener);
    }

    public void uploadImage(Bitmap imageBmp, String name, final Listener<String> listener) {
        reportModel.uploadImage(imageBmp, name, listener);
    }

}
