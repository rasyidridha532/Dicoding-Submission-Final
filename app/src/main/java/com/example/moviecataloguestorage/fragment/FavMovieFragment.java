package com.example.moviecataloguestorage.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.adapter.FavMovieAdapter;
import com.example.moviecataloguestorage.database.FavoriteHelper;
import com.example.moviecataloguestorage.model.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moviecataloguestorage.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavMovieFragment extends Fragment {

    @BindView(R.id.rv_movie_db)
    RecyclerView rvList;
    @BindView(R.id.tv_empty)
    TextView tvNoData;
    @BindView(R.id.sw_fav_movie)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Cursor cursor;
    private FavMovieAdapter adapter;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav_movie, container, false);

        ButterKnife.bind(this, v);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);

        adapter = new FavMovieAdapter(getActivity());
        adapter.setCursor(cursor);
        rvList.setAdapter(adapter);
        new loadMovieAsync(getActivity()).execute();

        progressBar.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);

//        getMovieDB();

        refreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
//            getMovieDB();
            refreshLayout.setRefreshing(false);
        }, 3000));

        return v;
    }

//    private void getMovieDB() {
//        FavoriteHelper favoriteHelper = new FavoriteHelper(getActivity().getApplicationContext());
//        favoriteHelper.open();
//        ArrayList<Movie> movies = favoriteHelper.getAllMovie();
//        FavMovieAdapter adapter;
//        if (movies.size() == 0) {
//            progressBar.setVisibility(View.GONE);
//            tvNoData.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.GONE);
//            tvNoData.setVisibility(View.GONE);
//            adapter = new FavMovieAdapter(movies, getActivity());
//            rvList.setAdapter(adapter);
//        }
//        favoriteHelper.close();
//    }

    private class loadMovieAsync extends AsyncTask<Void, Void, Cursor> {

        Activity activity;

        public loadMovieAsync(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver resolver = getActivity().getContentResolver();

            return resolver.query(CONTENT_URI_MOVIE, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor favorite) {
            super.onPostExecute(favorite);

            cursor = favorite;
            adapter.setCursor(cursor);
            adapter.notifyDataSetChanged();

            if (cursor.getCount() == 0) {
                progressBar.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }
}