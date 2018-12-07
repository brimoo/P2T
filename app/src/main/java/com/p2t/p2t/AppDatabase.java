package com.p2t.p2t;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class, Directory.class, File.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDAO userDAO();
    public abstract DirectoryDAO directoryDAO();
    public abstract FileDAO fileDAO();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database").allowMainThreadQueries().build();
        }

        return INSTANCE;
    }

    public static void destoryInstance() {
        INSTANCE = null;
    }
}
