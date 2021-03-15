package com.pettify.model.report;

import java.util.List;

public class ReportModelFireBase {
    public static final ReportModelFireBase instance = new ReportModelFireBase();

    private ReportModelFireBase() {
    }

    public void getAllReports(ReportModelSql.Listener<List<Report>> listener) {

    }

    public void addReport(Report report, ReportModelSql.EmptyListener listener) {

    }
}
