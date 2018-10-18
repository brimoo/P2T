package com.p2t.p2t;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface FileDAO {
    @Query("SELECT * FROM files where parentDirectoryID LIKE :uid")
    List<File> getFilesInDir(int uid);

    @Insert
    void insertAll(File... files);

    @Delete
    void delete(File file);
}
