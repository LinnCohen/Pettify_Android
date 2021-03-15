package com.pettify.model.report;

import android.os.AsyncTask;
import com.pettify.model.AppLocalDb;
import java.util.List;

public class ReportModelSql {
    public static final ReportModelSql instance = new ReportModelSql();

    private ReportModelSql() {
    }
    public interface Listener<T> {
        void onComplete(T data);
    }

    public interface EmptyListener {
        void onComplete();
    }

    public void getAllReports(final Listener<List<Report>> listener) {
        class MyAsyncTask extends AsyncTask{
            List<Report> data;
            @Override
            protected Object doInBackground(Object[] objects) {
                data = AppLocalDb.db.reportDao().getAll();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                listener.onComplete(data);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

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
}