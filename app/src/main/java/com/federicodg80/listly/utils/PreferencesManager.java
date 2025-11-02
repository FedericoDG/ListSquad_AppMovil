package com.federicodg80.listly.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private static final String PREFS_NAME = "listly_prefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_ISLOGGEDIN = "isloggedin";
    private static final String KEY_USER_ID = "user_uid";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveToken(Context context, String token) {
        getPreferences(context)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public static String getToken(Context context) {
        return getPreferences(context).getString(KEY_TOKEN, null);
    }

    public static void setLoggedIn(Context context, boolean isLoggedIn) {
        getPreferences(context)
                .edit()
                .putBoolean(KEY_ISLOGGEDIN, isLoggedIn)
                .apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getPreferences(context).getBoolean(KEY_ISLOGGEDIN, false);
    }

    public static void saveUserUId(Context context, String userId) {
        getPreferences(context)
                .edit()
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public static String getUserUId(Context context) {
        return getPreferences(context).getString(KEY_USER_ID, null);
    }

    public static void clearAll(Context context) {
        getPreferences(context)
                .edit()
                .clear()
                .apply();
    }
}
