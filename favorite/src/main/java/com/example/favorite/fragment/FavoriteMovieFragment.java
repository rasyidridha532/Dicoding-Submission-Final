package com.example.favorite.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.favorite.R;
import com.example.favorite.adapter.FavMovieAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favorite.database.DatabaseContract.MovieColumns.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {

    @BindView(R.id.rv_movie_db)
    RecyclerView rvMovie;
    @BindView(R.id.tv_empty)
    TextView tvNoData;

    private FavMovieAdapter adapter;
    private Cursor cursor;

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_movie, container, false);

        ButterKnife.bind(this, view);

        rvMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvMovie.setHasFixedSize(true);

        adapter = new FavMovieAdapter(getActivity());
        adapter.setCursor(cursor);
        rvMovie.setAdapter(adapter);

        new loadMovieAsync(getActivity()).execute();

        return view;
    }

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
            tvNoData.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

            if (cursor.getCount() == 0) {
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }
}
