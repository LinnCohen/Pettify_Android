package com.pettify.model.report;

import com.pettify.model.user.User;
import com.pettify.model.user.UserModel;

import java.util.List;

public class ReportModel {

    public final static ReportModel instance = new ReportModel();
    ReportModelFireBase reportModelFireBase = ReportModelFireBase.instance;
    ReportModelSql reportModelSql = ReportModelSql.instance;

    private ReportModel(){}

    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface EmptyListener {
        void onComplete();
    }

    public void getAllReports(final Listener<List<Report>> listener) {
        reportModelFireBase.getAllReports(listener);
    }

    public void addReport(final Report report, final ReportModelSql.EmptyListener listener) {
        reportModelFireBase.addReport(report, listener);
    }

    public void updateReport(final Report report, final EmptyListener listener) {
        reportModelFireBase.updateReport(report, listener);
    }

}
