package com.pettify.model.chat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface ChatDao {
    @Query("select * from Chat")
    LiveData<List<Chat>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Chat... chats);

    @Delete
    void delete(Chat chats);

}
