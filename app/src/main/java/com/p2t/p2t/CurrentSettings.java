package com.p2t.p2t;

public class CurrentSettings {
    private static boolean darkMode = false;

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
}
