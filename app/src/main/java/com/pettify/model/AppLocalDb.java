package com.pettify.model;

import androidx.room.Room;

public class AppLocalDb {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(PettifyApplication.context, AppLocalDbRepository.class, "pettify.db")
                    .fallbackToDestructiveMigration().build();
}
