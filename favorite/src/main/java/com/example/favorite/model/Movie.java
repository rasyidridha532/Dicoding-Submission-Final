package com.example.favorite.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.favorite.database.DatabaseContract.MovieColumns.*;
import static com.example.favorite.database.DatabaseContract.getColumnInt;
import static com.example.favorite.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {

    private int idMovie;

    private int page;

    private String id;

    private String title;

    private List<Integer> genreIds = new ArrayList<>();

    private String kategori;

    private String vote_average;

    private String poster_path;

    private String overview;

    public Movie() {}

    public Movie(Cursor cursor) {
        this.idMovie = getColumnInt(cursor, _ID);
        this.id = getColumnString(cursor, ID_MOVIE);
        this.title = getColumnString(cursor, TITLE);
        this.vote_average = getColumnString(cursor, RATING);
        this.poster_path = getColumnString(cursor, LINKPOSTER);
        this.overview = getColumnString(cursor, DESCRIPTION);
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idMovie);
        dest.writeInt(this.page);
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeList(this.genreIds);
        dest.writeString(this.kategori);
        dest.writeString(this.vote_average);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
    }

    protected Movie(Parcel in) {
        this.idMovie = in.readInt();
        this.page = in.readInt();
        this.id = in.readString();
        this.title = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.kategori = in.readString();
        this.vote_average = in.readString();
        this.poster_path = in.readString();
        this.overview = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
