package com.example.moviecataloguestorage.rest;

import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.model.MovieResponse;
import com.example.moviecataloguestorage.model.TVShow;
import com.example.moviecataloguestorage.model.TVShowResponse;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    String LANGUAGE ="en-US";

    @GET("discover/movie")
    Call<MovieResponse> getMovieRelease(@Query("api_key") String apiKey,
                                        @Query("primary_release_date.gte")String greaternow,
                                        @Query("primary_release_date.lte")String lessnow);

    @GET("discover/movie")
    Call<MovieResponse> getMovie(@Query("api_key") String apiKey,
                                 @Query("language") String language);

    @GET("movie/{id}")
    Call<Movie> getDetailMovie(@Path ("id") String id_movie,
                               @Query("api_key") String apiKey,
                               @Query("language") String language);

    @GET("search/movie")
    Call<MovieResponse> getSearchMovie(@Query("api_key") String apiKey,
                                       @Query("language") String language,
                                       @Query("query") String query);

    @GET("discover/tv")
    Call<TVShowResponse> getTVShow(@Query("api_key") String apiKey,
                                   @Query("language") String language);

    @GET("tv/{id}")
    Call<TVShow> getDetailTVShow(@Path("id") String id_tvshow,
                                 @Query("api_key") String apiKey,
                                 @Query("language") String language);

    @GET("search/tv")
    Call<TVShowResponse> getSearchTVShow(@Query("api_key") String apiKey,
                                         @Query("language") String language,
                                         @Query("query") String query);
}