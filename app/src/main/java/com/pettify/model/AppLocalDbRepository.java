package com.pettify.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pettify.model.message.Message;
import com.pettify.model.message.MessageDao;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportDao;

@Database(entities = {Report.class, Message.class}, version = 9)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract MessageDao messageDao();
}



