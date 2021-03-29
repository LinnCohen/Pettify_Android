package com.pettify.ui.report;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.pettify.model.listeners.EmptyListener;
import com.pettify.model.listeners.Listener;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.List;

public class ReportListViewModel extends ViewModel {
    private LiveData<List<Report>> reports = ReportModel.instance.getAllReports();
    private ReportModel reportModel;
    private UserModel userModel;

    public ReportListViewModel() {
        reportModel = ReportModel.instance;
        userModel = UserModel.instance;
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
        User user = userModel.getCurrentUser();
        if (user != null) {
            report.setReporterId(user.getId());
        }
        reportModel.addReport(report, listener);
    }

    public void updateReport(Report report, String reportId, EmptyListener listener) {
        User user = userModel.getCurrentUser();
        if (user != null) {
            report.setReporterId(user.getId());
        }
        reportModel.updateReport(report, reportId, listener);
    }

    public void uploadImage(Bitmap imageBmp, String name, final Listener<String> listener) {
        reportModel.uploadImage(imageBmp, name, listener);
    }

    public void deleteReport(String id, EmptyListener listener) {
        reportModel.deleteReport(id, listener);
    }

}
