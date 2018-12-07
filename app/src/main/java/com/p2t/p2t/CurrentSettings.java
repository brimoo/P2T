package com.p2t.p2t;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;

public class CurrentSettings {
    private static boolean darkMode = false;
    private static int currentUser = -1;

    public static void setMode(boolean b) {
        darkMode = b;
    }

    public static int getMode() {
        if (darkMode) return R.style.darkTheme;
        return R.style.lightTheme;
    }

    public static boolean getModeBool() {
        return darkMode;
    }

//    public static void syncMode(final Context context) {
//        if (currentUser == -1) {
//            return;
//        }
//
//        User user = AppDatabase.getAppDatabase(context).userDAO().getUserByID(currentUser);
//        user.setDarkMode(darkMode);
//    }

    public static void setCurrentUser(int uid) {
        currentUser = uid;
    }

    public static int getCurrentUser() {
        return currentUser;
    }
}
