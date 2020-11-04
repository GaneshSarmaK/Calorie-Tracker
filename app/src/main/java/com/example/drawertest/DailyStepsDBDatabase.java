package com.example.drawertest;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {DailyStepsDB.class}, version = 9, exportSchema = false)
public abstract class DailyStepsDBDatabase extends RoomDatabase {
    public abstract DailyStepsDBDao dailyStepsDBDao();
    private static volatile DailyStepsDBDatabase INSTANCE;
    static DailyStepsDBDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DailyStepsDBDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    DailyStepsDBDatabase.class, "dailyStepsDB_database")
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
