package com.p2t.p2t;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DirectoryDAO {
    @Query("SELECT * FROM directories where uid LIKE :uid")
    Directory getDirectory(int uid);

    @Query("SELECT * FROM directories where parent_directory_id LIKE :uid")
    List<Directory> getSubDirectories(int uid);

    @Insert
    void insertAll(Directory... directories);

    @Delete
    void delete(Directory directory);
}
