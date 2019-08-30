package com.example.moviecataloguestorage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.*;
import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.TITLE;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbfavorite";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_TABLE_MOVIE = "create table " + TABLE_MOVIE+
            " ("+_ID+ " integer primary key autoincrement, "+
            TITLE+" text not null, "+
            ID_MOVIE+" text not null, "+
            RATING+" text not null, "+
            LINKPOSTER+" text not null, "+
            DESCRIPTION+" text not null)";

    private static final String SQL_CREATE_TABLE_TVSHOW = "create table " + TABLE_TVSHOW+
            " ("+_ID+ " integer primary key autoincrement, "+
            TITLE_TV+" text not null, "+
            ID_TVSHOW+" text not null, "+
            RATING_TV+" text not null, "+
            LINKPOSTER_TV+" text not null, "+
            DESCRIPTION_TV+" text not null)";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOW);
        onCreate(db);
    }

}
