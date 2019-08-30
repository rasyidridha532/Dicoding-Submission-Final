package com.example.moviecataloguestorage.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moviecataloguestorage.BuildConfig;
import com.example.moviecataloguestorage.view.DetailActivity;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.TVShowAdapter;
import com.example.moviecataloguestorage.model.TVShow;
import com.example.moviecataloguestorage.model.TVShowResponse;
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
public class TVShowFragment extends Fragment {

    @BindView(R.id.rv_list_tvshow)
    RecyclerView rvList;
    @BindView(R.id.sw_layout_tv)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private TVShowAdapter adapter;



    public TVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tvshow, container, false);

        ButterKnife.bind(this, rootView);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);

        getTVShow();

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            getTVShow();
            swipeRefreshLayout.setRefreshing(false);
        }, 3000));

        return rootView;
    }

    private void getTVShow() {
        try {
            ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
            String apiKey = BuildConfig.API_TOKEN;
            Call<TVShowResponse> call = apiInterface.getTVShow(apiKey, ApiInterface.LANGUAGE);

            call.enqueue(new Callback<TVShowResponse>() {
                @Override
                public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    List<TVShow> tvShows = response.body().getResults();
                    adapter = new TVShowAdapter(getActivity(), tvShows);
                    rvList.setAdapter(adapter);
                    adapter.setListTVShow(tvShows);
                    pindahDetail(adapter, tvShows);
                }

                @Override
                public void onFailure(Call<TVShowResponse> call, Throwable t) {
                    Log.d("Error", t.getMessage());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), R.string.internet, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void pindahDetail(RecyclerView.Adapter adapter, final List<TVShow> tvShows) {
        rvList.setAdapter(adapter);
        ItemClickSupport.addTo(rvList).setOnItemClickListener((recyclerView, position, v) -> {
            TVShow tvList = tvShows.get(position);
            String id_tvshow = tvList.getId();
            clickDetail(tvShows.get(position));
        });
    }

    private void clickDetail(TVShow tvShows) {
        Intent detail = new Intent(getActivity(), DetailActivity.class);
        detail.putExtra("id_tvshow", tvShows.getId());
        startActivity(detail);
        getActivity().overridePendingTransition(0,0);
    }
}
