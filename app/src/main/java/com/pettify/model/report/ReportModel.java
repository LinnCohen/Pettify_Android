package com.pettify.model.report;

import androidx.lifecycle.MutableLiveData;

import com.pettify.model.Model;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.List;

public class ReportModel implements Model {

    public final static ReportModel instance = new ReportModel();
    ReportModelFireBase reportModelFireBase = ReportModelFireBase.instance;
    ReportModelSql reportModelSql = ReportModelSql.instance;

    private ReportModel(){}

    MutableLiveData<List<Report>> reportsList = new MutableLiveData<>();
    public MutableLiveData<List<Report>> getAllReports() {
        return reportsList;
    }

    public void refreshAllReports(EmptyListener listener) {
        reportModelFireBase.getAllReports(data -> {
            reportsList.setValue(data);
            listener.onComplete();
        });
    }

    public void addReport(final Report report, final ReportModel.EmptyListener listener) {
        reportModelFireBase.addReport(report, listener);
    }

    public void updateReport(final Report report, final EmptyListener listener) {
        reportModelFireBase.updateReport(report, listener);
    }

}
