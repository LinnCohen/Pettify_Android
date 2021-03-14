package com.pettify.model.report;

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

    public void getAllReports(final ReportModelSql.Listener<List<Report>> listener) {
        reportModelFireBase.getAllReports(listener);
    }

    public void addUser(final Report report, final ReportModelSql.EmptyListener listener) {
        reportModelFireBase.addReport(report, listener);
    }

}
