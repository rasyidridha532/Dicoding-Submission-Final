package com.example.moviecataloguestorage.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.MovieAdapter;
import com.example.moviecataloguestorage.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Movie> movieList = new ArrayList<>();
    private final Context context;

    public StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //gambar favorit
        movieList.clear();
        final long idToken = Binder.clearCallingIdentity();
        Cursor cursor = context.getContentResolver().query(CONTENT_URI_MOVIE, null,
                null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                movieList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        if (cursor != null) {
            cursor.close();
        }
        Binder.restoreCallingIdentity(idToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Movie movie;
        Bundle extras = new Bundle();
        extras.putInt(FavoriteMovieWidget.EXTRA_ITEM, position);
        Bitmap bitmap = null;
        try {
            movie = movieList.get(position);
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load("http://image.tmdb.org/t/p/w500" + movie.getPoster_path())
                    .apply(new RequestOptions().fitCenter())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

            extras.putString(MovieAdapter.EXTRA_MOVIE, movie.getTitle());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException | IndexOutOfBoundsException e) {
            Log.d("Widget Load Error", "error");
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        remoteViews.setImageViewBitmap(R.id.img_movie, bitmap);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.img_movie, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
