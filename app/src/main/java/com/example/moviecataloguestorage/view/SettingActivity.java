package com.example.moviecataloguestorage.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.notification.AlarmReceiver;
import com.example.moviecataloguestorage.notification.NotificationPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.daily_switch)
    Switch dailyReminder;
    @BindView(R.id.release_switch)
    Switch releaseReminder;
    @BindView(R.id.btn_change_language)
    Button btnLanguage;

    public NotificationPreference notificationPreference;
    public AlarmReceiver alarmReceiver;
    private Context context;
    private String KEY_DAILY  = "cekDaily";
    private String KEY_MOVIE_RELEASE  = "cekMovieRelease";
    private String RELEASE_MOVIE  = "release";
    private static final String RM_RECEIVE = "rRelease";
    public SharedPreferences filmRelease, daily;
    public SharedPreferences.Editor editRelease, editDaily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);

        alarmReceiver = new AlarmReceiver();

        btnLanguage.setOnClickListener(v -> {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        });
    }

    @OnCheckedChanged(R.id.daily_switch)
    public void setDailyReminder(boolean isChecked) {
        editDaily = daily.edit();
        if (isChecked) {
            editDaily.putBoolean(KEY_DAILY, true);
            editDaily.apply();
            reminderOn();
        } else {
            editDaily.putBoolean(KEY_DAILY, false);
            editDaily.apply();
            reminderOff();
        }

    }

    @OnCheckedChanged(R.id.release_switch)
    public void setReleaseReminder(boolean isChecked) {
        editRelease = filmRelease.edit();
        if (isChecked) {
            editRelease.putBoolean(KEY_MOVIE_RELEASE, true);
            editRelease.apply();
            //dibawah isi method
            releaseOn();
        } else {
            editRelease.putBoolean(KEY_MOVIE_RELEASE, false);
            editRelease.apply();
            //dibawah isi method
            releaseOff();
        }
    }

    public void reminderOn() {
        String time = "08:00";
        String message = "We're missing you!";

        notificationPreference.setDailyTime(time);
        notificationPreference.setDailyMessage(message);
        alarmReceiver.setReminder(SettingActivity.this, RM_RECEIVE, time, message);

        Toast.makeText(context, "Reminder ON", Toast.LENGTH_SHORT).show();
    }

    public void reminderOff() {
        alarmReceiver.cancelReminder(SettingActivity.this);

        Toast.makeText(context, "Reminder OFF", Toast.LENGTH_SHORT).show();
    }

    public void releaseOn() {

    }

    public void releaseOff() {

    }
}
