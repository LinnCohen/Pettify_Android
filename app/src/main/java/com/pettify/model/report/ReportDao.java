package com.pettify.model.report;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportDao {
    @Query("select * from Report")
    LiveData<List<Report>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Report... reports);

    @Delete
    void delete(Report report);

}
