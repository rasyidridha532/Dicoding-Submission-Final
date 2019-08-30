package com.example.favorite.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.favorite.R;
import com.example.favorite.adapter.FavMovieAdapter;
import com.example.favorite.adapter.FavTVShowAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.favorite.database.DatabaseContract.TVShowColumns.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTVShowFragment extends Fragment {

    @BindView(R.id.list_tvshow)
    RecyclerView rvTV;
    @BindView(R.id.tv_empty)
    TextView tvNoData;

    private FavTVShowAdapter adapter;
    private Cursor cursor;

    public FavoriteTVShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_tvshow, container, false);

        ButterKnife.bind(this, view);

        rvTV.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTV.setHasFixedSize(true);

        adapter = new FavTVShowAdapter(getActivity());
        adapter.setCursor(cursor);
        rvTV.setAdapter(adapter);

        new loadTVShowAsync(getActivity()).execute();

        return view;
    }

    private class loadTVShowAsync extends AsyncTask<Void, Void, Cursor> {

        Activity activity;

        public loadTVShowAsync(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver resolver = getActivity().getContentResolver();

            return resolver.query(CONTENT_URI_TVSHOW, null, null, null, null);
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
