package com.p2t.p2t;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "directories")
public class Directory {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "parent_directory_id")
    private int parentDirectoryID;

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

    public int getParentDirectoryID() {
        return parentDirectoryID;
    }

    public void setParentDirectoryID(int parentDirectoryID) {
        this.parentDirectoryID = parentDirectoryID;
    }
}
