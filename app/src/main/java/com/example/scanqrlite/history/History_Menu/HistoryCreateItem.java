package com.example.scanqrlite.history.History_Menu;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "create_item")
public class HistoryCreateItem {
    @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "title_create")
    String title;
    @ColumnInfo(name = "content_create")
    String content;
    @ColumnInfo(name = "date_create")
    String date;
    @ColumnInfo(name = "result_create")
    String result;
    @ColumnInfo(name = "security_create")
    String security;
    @ColumnInfo(name = "password_create")
    String password;

    public HistoryCreateItem(String title, String content, String date, String result) {
        this.title = title;
        this.content = content;
        this.date = date;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
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

}
