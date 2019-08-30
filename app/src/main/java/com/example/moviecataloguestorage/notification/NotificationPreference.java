package com.example.moviecataloguestorage.notification;

import android.content.Context;
import android.content.SharedPreferences;

public class NotificationPreference {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String PREFNAME = "pref";
    private static final String DAILY = "DAILY";
    private static final String DATE = "DATE";
    private static final String MESSAGE = "MESSAGE";

    public NotificationPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDailyTime(String time) {
        editor.putString(DAILY, time);
        editor.apply();
    }

    public void setDailyMessage(String message) {
        editor.putString(MESSAGE, message);
        editor.apply();
    }

    public void setMovieReleaseDate(String date) {
        editor.putString(DATE, date);
        editor.apply();
    }

    public void setMovieRelease(String message) {
        editor.putString(MESSAGE, message);
    }

}
