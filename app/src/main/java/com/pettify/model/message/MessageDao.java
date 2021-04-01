package com.pettify.model.message;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import java.util.List;

@Dao
public interface MessageDao {
    @Query("select * from Message")
    LiveData<List<Message>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Message... messages);

    @Delete
    void delete(Message chats);

}
