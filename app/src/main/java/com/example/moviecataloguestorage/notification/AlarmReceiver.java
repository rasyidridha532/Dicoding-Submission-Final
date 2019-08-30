package com.example.moviecataloguestorage.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.model.Movie;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {

    private final int NOTIFY_RELEASE = 21;
    public static final String MOVIE_ID = "MOVIE_ID";
    public List<Movie> listMovie = new ArrayList<>(), tmpMovies;
    public static String CHANNEL_ID = "ch_1";

    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int NOTIF_ID_ONETIME = 100;
    private final int NOTIF_ID_REPEATING = 101;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }

    public void setReminder(Context context, String type, String time, String message) {
        String DATE_FORMAT = "yyyy-MM-dd";
        String TIME_FORMAT = "HH:mm";

        if (isTimeInvalid(time, TIME_FORMAT)) return;

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_ONETIME, intent, 0);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,NOTIFY_RELEASE,intent,0);

        if(alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }

    public boolean isTimeInvalid(String time, String format) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            dateFormat.setLenient(false);
            dateFormat.parse(time);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    private void showNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_local_movies_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(notifId, notification);
        }
    }
}