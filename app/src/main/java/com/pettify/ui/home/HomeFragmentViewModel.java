package com.pettify.ui.home;

import android.util.Log;

import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeFragmentViewModel extends ViewModel {
    private LiveData<List<Report>> reports = ReportModel.instance.getAllReports();

    public LiveData<List<Report>> getReports() {
        return reports;
    }
}