package com.example.moviecataloguestorage.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moviecataloguestorage.database.FavoriteHelper;

import static com.example.moviecataloguestorage.database.DatabaseContract.AUTHORITY;
import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.TABLE_MOVIE;
import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.CONTENT_URI_TVSHOW;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.TABLE_TVSHOW;

public class FavoriteProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV_SHOW = 3;
    private static final int TV_SHOW_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE, MOVIE);

        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE + "/#", MOVIE_ID);

        sUriMatcher.addURI(AUTHORITY, TABLE_TVSHOW, TV_SHOW);

        sUriMatcher.addURI(AUTHORITY, TABLE_TVSHOW + "/#", TV_SHOW_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = new FavoriteHelper(getContext());
        favoriteHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        favoriteHelper.open();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = favoriteHelper.queryProvider();
                break;
            case TV_SHOW:
                cursor = favoriteHelper.queryProviderTVShow();
                break;
            case MOVIE_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW_ID:
                cursor = favoriteHelper.queryByIdProviderTVShow(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        favoriteHelper.open();
        long added;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                added = favoriteHelper.insertProvider(values);
                uri = Uri.parse(CONTENT_URI_MOVIE + "/" + added);
                break;
            case TV_SHOW:
                added = favoriteHelper.insertProviderTVShow(values);
                uri = Uri.parse(CONTENT_URI_TVSHOW + "/" + added);
                break;
            default:
                added = 0;
                break;
        }
        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case TV_SHOW_ID:
                deleted = favoriteHelper.deleteProviderTVShow(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int update;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_ID:
                update = favoriteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            case TV_SHOW_ID:
                update = favoriteHelper.updateProviderTVShow(uri.getLastPathSegment(), values);
                break;
            default:
                update = 0;
                break;
        }

        if (update > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return update;
    }
}
