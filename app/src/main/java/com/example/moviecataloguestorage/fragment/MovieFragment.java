package com.example.moviecataloguestorage.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviecataloguestorage.BuildConfig;
import com.example.moviecataloguestorage.view.DetailActivity;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.MovieAdapter;
import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.model.MovieResponse;
import com.example.moviecataloguestorage.rest.Api;
import com.example.moviecataloguestorage.rest.ApiInterface;
import com.example.moviecataloguestorage.rest.ItemClickSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.sw_layout_movie)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_list_movie)
    RecyclerView rvList;

    private MovieAdapter adapter;

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        ButterKnife.bind(this, rootView);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);
        getMovie();

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            getMovie();
            swipeRefreshLayout.setRefreshing(false);
        }, 3000));

        return rootView;
    }

    private void pindahDetail(RecyclerView.Adapter adapter, final List<Movie> movieItems) {
        rvList.setAdapter(adapter);
        ItemClickSupport.addTo(rvList).setOnItemClickListener((recyclerView, position, v) -> {
            Movie movieList = movieItems.get(position);
            String id_movie = movieList.getId();
            clickDetail(movieItems.get(position));
        });
    }

    private void clickDetail(Movie movieItems) {
        Intent detail = new Intent(getActivity(), DetailActivity.class);
        detail.putExtra("id_movie",movieItems.getId());
        startActivity(detail);
        getActivity().overridePendingTransition(0,0);
    }

    private void getMovie() {
        try {
            ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
            String apiKey = BuildConfig.API_TOKEN;
            Call<MovieResponse> call = apiInterface.getMovie(apiKey, ApiInterface.LANGUAGE);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    List<Movie> movies = response.body().getResults();
                    adapter = new MovieAdapter(getActivity(),movies);
                    rvList.setAdapter(adapter);
                    adapter.setListMovie(movies);
                    pindahDetail(adapter, movies);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.internet, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }


}