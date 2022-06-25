package com.example.scanqrlite.history.History_Menu.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;

import java.util.List;

@Dao
public interface CreateItemDAO {

    @Insert
    void insertItem(HistoryCreateItem historyCreateItem);

    @Query("SELECT * FROM create_item ORDER BY id DESC")
    List<HistoryCreateItem> getListItem();
}
