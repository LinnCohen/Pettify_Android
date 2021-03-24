package com.pettify.model.report;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pettify.model.AppLocalDb;
import com.pettify.model.listeners.EmptyListener;

import java.util.List;

public class ReportModelSql {
    public static final ReportModelSql instance = new ReportModelSql();

    private ReportModelSql() {
    }


    public LiveData<List<Report>> getAllReports() {
       return AppLocalDb.db.reportDao().getAll();
    }

    //TODO - fix deprecated code
    public void addReport(final Report report, final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.reportDao().insertAll(report);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

    public void deleteReport(Report report,  final EmptyListener listener) {
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.reportDao().delete(report);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if (listener != null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
}