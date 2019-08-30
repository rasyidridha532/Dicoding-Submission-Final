package com.example.moviecataloguestorage.view.search;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviecataloguestorage.BuildConfig;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.TVShowAdapter;
import com.example.moviecataloguestorage.model.TVShow;
import com.example.moviecataloguestorage.model.TVShowResponse;
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

public class SearchTVShowActivity extends AppCompatActivity {

    @BindView(R.id.rv_search_tvshow)
    RecyclerView rvTVShow;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    SearchManager searchManager;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    List<TVShow> tvShowList = new ArrayList<>();

    TVShowAdapter tvShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tvshow);

        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(R.string.search_tvshow);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvEmpty.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        rvTVShow.setLayoutManager(new LinearLayoutManager(this));
        rvTVShow.setHasFixedSize(true);

        tvShowAdapter = new TVShowAdapter(this, tvShowList);
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
                        getSearchTVShow(newText);
                    }
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void pindahDetail(RecyclerView.Adapter adapter, final List<TVShow> tvShows) {
        rvTVShow.setAdapter(adapter);
        ItemClickSupport.addTo(rvTVShow).setOnItemClickListener((recyclerView, position, v) -> {
            TVShow tvList = tvShows.get(position);
            String id_tvshow = tvList.getId();
            clickDetail(tvShows.get(position));
        });
    }

    private void clickDetail(TVShow tvShows) {
        Intent detail = new Intent(this, DetailActivity.class);
        detail.putExtra("id_tvshow", tvShows.getId());
        startActivity(detail);
        this.overridePendingTransition(0,0);
    }

    private void getSearchTVShow(String title) {
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        String apiKey = BuildConfig.API_TOKEN;
        Call<TVShowResponse> call = apiInterface.getSearchTVShow(apiKey, ApiInterface.LANGUAGE, title);

        call.enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                List<TVShow> tvShows = response.body().getResults();
                tvShowAdapter = new TVShowAdapter(SearchTVShowActivity.this, tvShows);
                tvShowList.addAll(tvShows);
                rvTVShow.setAdapter(tvShowAdapter);
                tvShowAdapter.notifyDataSetChanged();
                if (tvShows.size() == 0) {
                    tvEmpty.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    tvEmpty.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    tvShowAdapter.setListTVShow(tvShows);
                    pindahDetail(tvShowAdapter, tvShows);
                }
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SearchTVShowActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
