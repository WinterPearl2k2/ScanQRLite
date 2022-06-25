package com.example.scanqrlite.history.History_Menu.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;

@Database(entities = {HistoryCreateItem.class}, version = 1)
public abstract class CreateDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "create.db";
    private static CreateDatabase instance;

    public static synchronized CreateDatabase getInstance(Context context) {
        if(instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(), CreateDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries().build();
        return instance;
    }

    public abstract CreateItemDAO createItemDAO();
}
