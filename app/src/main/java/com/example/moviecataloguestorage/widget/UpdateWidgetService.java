package com.example.moviecataloguestorage.widget;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.widget.RemoteViews;

import com.example.moviecataloguestorage.R;

public class UpdateWidgetService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_item);
        ComponentName widget = new ComponentName(this, FavoriteMovieWidget.class);

        manager.updateAppWidget(widget, remoteViews);
        jobFinished(params, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
