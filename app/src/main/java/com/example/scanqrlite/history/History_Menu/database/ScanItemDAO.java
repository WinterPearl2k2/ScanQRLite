package com.example.scanqrlite.history.History_Menu.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.scanqrlite.history.History_Menu.HistoryCreateItem;
import com.example.scanqrlite.history.History_Menu.HistoryScanItem;

import java.util.List;

@Dao
public interface ScanItemDAO {

    @Insert
    void insertItem(HistoryScanItem historyScanItem);

    @Query("SELECT * FROM scan_item ORDER BY id DESC")
    List<HistoryScanItem> getListItem();
}
