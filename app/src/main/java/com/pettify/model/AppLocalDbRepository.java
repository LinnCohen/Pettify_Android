package com.pettify.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pettify.model.report.Report;
import com.pettify.model.report.ReportDao;
import com.pettify.model.user.User;
import com.pettify.model.user.UserDao;

@Database(entities = {User.class, Report.class}, version = 6)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract UserDao userDao();
}



