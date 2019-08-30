package com.example.favorite.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.example.favorite.database.DatabaseContract.TVShowColumns.*;
import static com.example.favorite.database.DatabaseContract.getColumnInt;
import static com.example.favorite.database.DatabaseContract.getColumnString;

public class TVShow implements Parcelable {

    private int idTVShow;

    private int page;

    private String id;

    private String name;

    private String vote_average;

    private String kategori;

    private List<Integer> genreIds = new ArrayList<Integer>();

    private String poster_path;

    private String overview;

    public TVShow() {}

    public TVShow (Cursor cursor) {
        this.idTVShow = getColumnInt(cursor, _ID);
        this.id = getColumnString(cursor, ID_TVSHOW);
        this.name = getColumnString(cursor, TITLE_TV);
        this.vote_average = getColumnString(cursor, RATING_TV);
        this.poster_path = getColumnString(cursor, LINKPOSTER_TV);
        this.overview = getColumnString(cursor, DESCRIPTION_TV);
    }

    public int getIdTVShow() {
        return idTVShow;
    }

    public void setIdTVShow(int idTVShow) {
        this.idTVShow = idTVShow;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.idTVShow);
        dest.writeInt(this.page);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.vote_average);
        dest.writeString(this.kategori);
        dest.writeList(this.genreIds);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
    }

    protected TVShow(Parcel in) {
        this.idTVShow = in.readInt();
        this.page = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.vote_average = in.readString();
        this.kategori = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.poster_path = in.readString();
        this.overview = in.readString();
    }

    public static final Creator<TVShow> CREATOR = new Creator<TVShow>() {
        @Override
        public TVShow createFromParcel(Parcel source) {
            return new TVShow(source);
        }

        @Override
        public TVShow[] newArray(int size) {
            return new TVShow[size];
        }
    };
}
