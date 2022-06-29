package com.example.scanqrlite.history.History_Menu;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scan_item")
public class HistoryScanItem {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "title_scan")
    String title;
    @ColumnInfo(name = "content_scan")
    String content;
    @ColumnInfo(name = "date_scan")
    String date;
    @ColumnInfo(name = "result_scan")
    String result;
    @ColumnInfo(name = "security_scan")
    String security;
    @ColumnInfo(name = "password_scan")
    String password;
    @ColumnInfo(name = "type_scan")
    String typeScan;

    public HistoryScanItem(String title, String content, String date, String result) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public String getTypeScan() {
        return typeScan;
    }

    public void setTypeScan(String typeScan) {
        this.typeScan = typeScan;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
