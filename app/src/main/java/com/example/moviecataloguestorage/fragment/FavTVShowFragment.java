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
import com.example.moviecataloguestorage.adapter.FavTVShowAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.moviecataloguestorage.database.DatabaseContract.TVShowColumns.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavTVShowFragment extends Fragment {

    @BindView(R.id.rv_tvshow_db)
    RecyclerView rvList;
    @BindView(R.id.tv_empty)
    TextView tvNoData;
    @BindView(R.id.sw_fav_tvshow)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private Cursor cursor;
    private FavTVShowAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fav_tvshow, container, false);
        ButterKnife.bind(this, v);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);

        adapter = new FavTVShowAdapter(getActivity());
        adapter.setCursor(cursor);
        rvList.setAdapter(adapter);
        new loadTVShowAsync(getActivity()).execute();

        progressBar.setVisibility(View.GONE);
        tvNoData.setVisibility(View.GONE);

//        getTVShowDB();

        refreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
//            getTVShowDB();
            refreshLayout.setRefreshing(false);
        }, 3000));



        return v;
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
        protected void onPostExecute(Cursor tvshow) {
            super.onPostExecute(tvshow);

            cursor = tvshow;
            adapter.setCursor(cursor);
            adapter.notifyDataSetChanged();

            if (cursor.getCount() == 0) {
                progressBar.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }
}
