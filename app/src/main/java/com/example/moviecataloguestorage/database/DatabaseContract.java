package com.example.moviecataloguestorage.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.example.moviecataloguestorage";
    private static final String SCHEME = "content";

    private DatabaseContract(){}

    public static final class MovieColumns implements BaseColumns {
        public static String TABLE_MOVIE = "table_movie";

        public static String TITLE =  "title";
        public static String ID_MOVIE = "id_movie";
        public static String RATING = "rating";
        public static String LINKPOSTER = "linkPoster";
        public static String DESCRIPTION = "description";

        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
    }

    public static final class TVShowColumns implements BaseColumns {
        public static String TABLE_TVSHOW = "table_tvshow";

        public static String TITLE_TV =  "title_tv";
        public static String ID_TVSHOW = "id_tvshow";
        public static String RATING_TV = "rating_tv";
        public static String LINKPOSTER_TV = "linkPoster_tv";
        public static String DESCRIPTION_TV = "description_tv";

        public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_TVSHOW)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName) );
    }
}