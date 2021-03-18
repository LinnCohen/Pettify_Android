package com.pettify.model.report;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pettify.model.AppLocalDb;
import com.pettify.model.Model;
import com.pettify.model.user.User;

import java.util.List;

public class ReportModelSql {
    public static final ReportModelSql instance = new ReportModelSql();

    private ReportModelSql() {
    }


    public LiveData<List<Report>> getAllReports() {
       return AppLocalDb.db.reportDao().getAll();
    }

    public void addReport(final Report report, final Model.EmptyListener listener) {
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
}