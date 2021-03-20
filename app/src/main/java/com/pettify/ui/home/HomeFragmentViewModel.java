package com.pettify.ui.home;

import android.util.Log;

import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class HomeFragmentViewModel extends ViewModel {

    private LiveData<List<Report>> myReports;
    Report report;

    public LiveData<List<Report>> getData(){
        if (myReports == null)
            myReports =  ReportModel.instance.getAllReports();

//        for(Report report : myReports.getValue()) {
//            Log.d("report",report.getDescription() + report.getId());
//        }
        return myReports;
    }



    public HomeFragmentViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}