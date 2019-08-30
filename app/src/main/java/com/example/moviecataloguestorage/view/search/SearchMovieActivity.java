package com.example.moviecataloguestorage.view.search;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviecataloguestorage.BuildConfig;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.MovieAdapter;
import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.model.MovieResponse;
import com.example.moviecataloguestorage.rest.Api;
import com.example.moviecataloguestorage.rest.ApiInterface;
import com.example.moviecataloguestorage.rest.ItemClickSupport;
import com.example.moviecataloguestorage.view.DetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMovieActivity extends AppCompatActivity{

    @BindView(R.id.rv_search_movie)
    RecyclerView rvMovie;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    SearchManager searchManager;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    List<Movie> movieList = new ArrayList<>();

    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movie);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.search_movie);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvEmpty.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        rvMovie.setLayoutManager(new LinearLayoutManager(this));
        rvMovie.setHasFixedSize(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_here));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!newText.equals("")) {
                        progressBar.setVisibility(View.VISIBLE);
                        getSearchMovie(newText);
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void pindahDetail(RecyclerView.Adapter adapter, final List<Movie> movieItems) {
        rvMovie.setAdapter(adapter);
        ItemClickSupport.addTo(rvMovie).setOnItemClickListener((recyclerView, position, v) -> {
            Movie movieList = movieItems.get(position);
            String id_movie = movieList.getId();
            clickDetail(movieItems.get(position));
        });
    }

    private void clickDetail(Movie movieItems) {
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra("id_movie",movieItems.getId());
        startActivity(detail);
        this.overridePendingTransition(0,0);
    }

    private void getSearchMovie(String query) {
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        String apiKey = BuildConfig.API_TOKEN;
        Call<MovieResponse> call = apiInterface.getSearchMovie(apiKey, ApiInterface.LANGUAGE, query);

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                movieAdapter = new MovieAdapter(SearchMovieActivity.this, movies);
                movieList.addAll(movies);
                rvMovie.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
                if (movies.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    movieAdapter.setListMovie(movies);
                    pindahDetail(movieAdapter, movies);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Toast.makeText(SearchMovieActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        });
    }
}