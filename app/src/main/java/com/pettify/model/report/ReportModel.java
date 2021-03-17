package com.pettify.model.report;

import com.pettify.model.Model;
import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.List;

public class ReportModel implements Model {

    public final static ReportModel instance = new ReportModel();
    ReportModelFireBase reportModelFireBase = ReportModelFireBase.instance;
    ReportModelSql reportModelSql = ReportModelSql.instance;

    private ReportModel(){}

    public void getAllReports(final Listener<List<Report>> listener) {
        reportModelFireBase.getAllReports(listener);
    }

    public void addReport(final Report report, final ReportModel.EmptyListener listener) {
        reportModelFireBase.addReport(report, listener);
    }

    public void updateReport(final Report report, final EmptyListener listener) {
        reportModelFireBase.updateReport(report, listener);
    }

}
