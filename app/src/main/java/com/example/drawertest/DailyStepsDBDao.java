package com.example.drawertest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DailyStepsDBDao {
    @Query("SELECT * FROM dailyStepsDB")
    List<DailyStepsDB> getAll();
    @Query("SELECT * FROM dailyStepsDB WHERE reportTime LIKE :reportTime LIMIT 1")
    DailyStepsDB findDailyStepsDB(String reportTime);
    @Insert
    void insertAll(DailyStepsDB... dailyStepsDB);
    @Insert
    long insert(DailyStepsDB dailyStepsDB);
    @Delete
    void delete(DailyStepsDB dailyStepsDB);
    @Update(onConflict = REPLACE)
    public void updateDailyStepsDB(DailyStepsDB... dailyStepsDB);
    @Query("DELETE FROM dailyStepsDB")
    void deleteAll();
}