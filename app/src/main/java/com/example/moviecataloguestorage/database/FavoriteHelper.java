package com.example.moviecataloguestorage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.model.TVShow;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.*;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.DESCRIPTION_TV;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.ID_TVSHOW;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.LINKPOSTER_TV;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.RATING_TV;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.TABLE_TVSHOW;
import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.TITLE_TV;

public class FavoriteHelper {
    private static String DATABASE_TABLE_MOVIE = TABLE_MOVIE;
    private static String DATABASE_TABLE_TVSHOW = TABLE_TVSHOW;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<Movie> getAllMovie() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        database = databaseHelper.getReadableDatabase();

        String query = " SELECT * FROM " + DATABASE_TABLE_MOVIE + " ORDER BY " + _ID + " DESC";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();

                movie.setIdMovie(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID_MOVIE)));
                movie.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(RATING)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(LINKPOSTER)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public boolean checkIsMovieAlreadyFavorited(String id) {
        String query = "SELECT * FROM " + TABLE_MOVIE + " WHERE " + ID_MOVIE + " = '" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean checkIsTVShowAlreadyFavorited(String id) {
        String query = "SELECT * FROM " + TABLE_TVSHOW + " WHERE " + ID_TVSHOW + " = '" + id + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public ArrayList<Movie> getAllTVShow() {
        ArrayList<Movie> arrayList = new ArrayList<>();
        database = databaseHelper.getReadableDatabase();

        String query = " SELECT * FROM " + DATABASE_TABLE_TVSHOW + " ORDER BY " + _ID + " DESC";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();

        Movie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new Movie();

                movie.setIdMovie(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE_TV)));
                movie.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID_TVSHOW)));
                movie.setVote_average(cursor.getString(cursor.getColumnIndexOrThrow(RATING_TV)));
                movie.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(LINKPOSTER_TV)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION_TV)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insertMovie(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE, movie.getTitle());
        contentValues.put(ID_MOVIE, movie.getId());
        contentValues.put(RATING, movie.getVote_average());
        contentValues.put(LINKPOSTER, movie.getPoster_path());
        contentValues.put(DESCRIPTION, movie.getOverview());

        return database.insert(DATABASE_TABLE_MOVIE, null, contentValues);
    }

    public long insertTVShow(TVShow tvShow) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(TITLE_TV, tvShow.getName());
        contentValues.put(ID_TVSHOW, tvShow.getId());
        contentValues.put(RATING_TV, tvShow.getVote_average());
        contentValues.put(LINKPOSTER_TV, tvShow.getPoster_path());
        contentValues.put(DESCRIPTION_TV, tvShow.getOverview());

        return database.insert(DATABASE_TABLE_MOVIE, null, contentValues);
    }

    public void beginTransaction() {
        database.beginTransaction();
    }

    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTransaction() {
        database.endTransaction();
    }

    public int deleteMovie(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, ID_MOVIE + " = ?", new String[] {id});
    }

    public int deleteTVShow(String id) {
        return database.delete(DATABASE_TABLE_TVSHOW, ID_TVSHOW + " = ?", new String[] {id});
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE_MOVIE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE_MOVIE
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public Cursor queryByIdProviderTVShow(String id) {
        return database.query(DATABASE_TABLE_TVSHOW, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProviderTVShow() {
        return database.query(DATABASE_TABLE_TVSHOW
                , null
                , null
                , null
                , null
                , null
                , _ID + " DESC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE_MOVIE, null, values);
    }

    public long insertProviderTVShow(ContentValues values) {
        return database.insert(DATABASE_TABLE_TVSHOW, null, values);
    }

    public int updateProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE_MOVIE,values,_ID +" = ?",new String[]{id} );
    }

    public int updateProviderTVShow(String id, ContentValues values){
        return database.update(DATABASE_TABLE_TVSHOW,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE_MOVIE, ID_MOVIE + " = ?", new String[]{id});
    }

    public int deleteProviderTVShow(String id) {
        return database.delete(DATABASE_TABLE_TVSHOW, ID_TVSHOW + " = ?", new String[]{id});
    }
}