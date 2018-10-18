package com.p2t.p2t;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "files")
public class File {

    @PrimaryKey
    private int parentDirectoryID;

    @ColumnInfo(name = "uid")
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    @ColumnInfo(name = "text")
    private String text;

    public int getParentDirectoryID() {
        return parentDirectoryID;
    }

    public void setParentDirectoryID(int parentDirectoryID) {
        this.parentDirectoryID = parentDirectoryID;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
