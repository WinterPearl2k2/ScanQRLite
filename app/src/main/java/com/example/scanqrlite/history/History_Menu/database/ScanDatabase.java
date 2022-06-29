package com.example.scanqrlite.history.History_Menu.database;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.scanqrlite.history.History_Menu.HistoryScanItem;

@Database(entities = {HistoryScanItem.class}, version = 1)
public abstract class ScanDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "scan.db";
    private static ScanDatabase instance;

    public static synchronized ScanDatabase getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), ScanDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries().build();
        return instance;
    }

    public abstract ScanItemDAO scanItemDAO();
}
