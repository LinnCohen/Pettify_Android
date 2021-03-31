package com.pettify.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.pettify.model.chat.Chat;
import com.pettify.model.chat.ChatDao;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportDao;

@Database(entities = {Report.class, Chat.class}, version = 8)
public abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract ReportDao reportDao();
    public abstract ChatDao chatDao();
}



