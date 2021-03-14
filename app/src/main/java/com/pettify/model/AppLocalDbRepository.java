package com.pettify.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pettify.model.report.ReportDao;
import com.pettify.model.user.User;
import com.pettify.model.user.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ReportDao reportDao();
}



