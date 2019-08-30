package com.example.moviecataloguestorage.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.MovieAdapter;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class FavoriteMovieWidget extends AppWidgetProvider {

    int idWidget;

    private static final String TOAST_ACTION = "com.example.moviecataloguestorage.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.moviecataloguestorage.EXTRA_ITEM";
    public static final String EXTRA_MOVIE_FAV = "com.example.moviecatalogue.FAVORITE";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Intent intent = new Intent(context, MovieWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_movie_widget);
        views.setRemoteAdapter(R.id.stack_view_movie, intent);
        views.setEmptyView(R.id.stack_view_movie, R.id.empy_widget);

        Intent toastIntent = new Intent(context, FavoriteMovieWidget.class);
        toastIntent.setAction(FavoriteMovieWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.stack_view_movie, toastPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        switch (Objects.requireNonNull(intent.getAction())) {
            case TOAST_ACTION:
                idWidget = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                String judul = intent.getStringExtra(MovieAdapter.EXTRA_MOVIE);
                Toast.makeText(context, judul, Toast.LENGTH_SHORT).show();
                break;

            case EXTRA_MOVIE_FAV:
                int idWidgets[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, FavoriteMovieWidget.class));
                onUpdate(context, appWidgetManager, idWidgets);
                appWidgetManager.notifyAppWidgetViewDataChanged(idWidgets, R.id.stack_view_movie);
                break;
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

