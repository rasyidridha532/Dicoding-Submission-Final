package com.example.moviecataloguestorage.view;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.example.moviecataloguestorage.BuildConfig;
import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.database.FavoriteHelper;
import com.example.moviecataloguestorage.widget.UpdateWidgetService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviecataloguestorage.model.Movie;
import com.example.moviecataloguestorage.model.TVShow;
import com.example.moviecataloguestorage.rest.Api;
import com.example.moviecataloguestorage.rest.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String ID_MOVIE = "id_movie";
    public static final String ID_TVSHOW = "id_tvshow";

    @BindView(R.id.tv_title_detail)
    TextView tvTitleDetail;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.img_poster_detail)
    ImageView imgPoster;
    @BindView(R.id.tv_synopsis)
    TextView tvDescription;

    @BindView(R.id.fab_favorite)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String apiKey = BuildConfig.API_TOKEN;

    FavoriteHelper favoriteHelper;

    boolean exist;
    boolean checked = false;

    String id_movie, id_tvshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + i == 0) {
                    collapsingToolbarLayout.setTitle("Detail");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

        id_movie = getIntent().getStringExtra("id_movie");
        id_tvshow = getIntent().getStringExtra("id_tvshow");

        if (id_movie != null) {
            getDetailMovie(id_movie);
        }
        if (id_tvshow != null) {
            getDetailTVShow(id_tvshow);
        }
    }

    private void getDetailMovie(String id_movie) {
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<Movie> call = apiInterface.getDetailMovie(id_movie,apiKey,ApiInterface.LANGUAGE);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                progressBar.setVisibility(View.GONE);
                tvTitleDetail.setText(response.body().getTitle());
                tvRating.setText("Rating: "+response.body().getVote_average());

                String poster = response.body().getPoster_path();
                Glide.with(getApplicationContext())
                        .load("http://image.tmdb.org/t/p/w500"+poster)
                        .into(imgPoster);

                tvDescription.setText(response.body().getOverview());

                favoriteHelper = new FavoriteHelper(getApplicationContext());
                favoriteHelper.open();
                exist = favoriteHelper.checkIsMovieAlreadyFavorited(id_movie);
                if (exist) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_saved));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_unsaved));
                }

                fab.setOnClickListener(v -> {
                    if (!exist && !checked) {
                        exist = true;
                        checked = true;
                        String favTitle = tvTitleDetail.getText().toString();
                        String favRating = tvRating.getText().toString().substring(8);
                        String favDesc = tvDescription.getText().toString();

                        Movie mov = new Movie();
                        mov.setTitle(favTitle);
                        mov.setVote_average(favRating);
                        mov.setId(id_movie);
                        mov.setPoster_path(poster);
                        mov.setOverview(favDesc);

                        favoriteHelper.beginTransaction();
                        favoriteHelper.insertMovie(mov);
                        favoriteHelper.setTransactionSuccess();
                        favoriteHelper.endTransaction();
                        startJob();
                        fab.setImageResource(R.drawable.fav_saved);

                        Toast.makeText(getApplication(), R.string.save_success, Toast.LENGTH_SHORT).show();
                    } else if (checked) {
                        checked = false;
                        exist = false;
                        favoriteHelper.deleteMovie(id_movie);
                        fab.setImageResource(R.drawable.fav_unsaved);
                        startJob();
                        Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
                    } else {
                        //hapus favorite
                        exist = false;
                        favoriteHelper.deleteMovie(id_movie);
                        fab.setImageResource(R.drawable.fav_unsaved);
                        Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(DetailActivity.this, R.string.internet, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailTVShow(String id_tvshow) {
        ApiInterface apiInterface = Api.getUrl().create(ApiInterface.class);
        Call<TVShow> call = apiInterface.getDetailTVShow(id_tvshow,apiKey,ApiInterface.LANGUAGE);

        call.enqueue(new Callback<TVShow>() {
            @Override
            public void onResponse(Call<TVShow> call, Response<TVShow> response) {
                progressBar.setVisibility(View.GONE);
                tvTitleDetail.setText(response.body().getName());
                tvRating.setText("Rating: " + response.body().getVote_average());

                String poster = response.body().getPoster_path();
                Glide.with(getApplicationContext())
                        .load("http://image.tmdb.org/t/p/w500"+poster)
                        .into(imgPoster);

                tvDescription.setText(response.body().getOverview());

                favoriteHelper = new FavoriteHelper(getApplicationContext());
                favoriteHelper.open();
                exist = favoriteHelper.checkIsTVShowAlreadyFavorited(id_tvshow);
                if (exist) {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_saved));
                } else {
                    fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_unsaved));
                }

                fab.setOnClickListener(v -> {
                    if (!exist && !checked) {
                        exist = true;
                        checked = true;
                        String favTitle = tvTitleDetail.getText().toString();
                        String favRating = tvRating.getText().toString().substring(8);
                        String favDesc = tvDescription.getText().toString();

                        TVShow tv = new TVShow();
                        tv.setName(favTitle);
                        tv.setVote_average(favRating);
                        tv.setId(id_tvshow);
                        tv.setPoster_path(poster);
                        tv.setOverview(favDesc);

                        favoriteHelper.beginTransaction();
                        favoriteHelper.insertTVShow(tv);
                        favoriteHelper.setTransactionSuccess();
                        favoriteHelper.endTransaction();
                        fab.setImageResource(R.drawable.fav_saved);
                        startJob();
                        Toast.makeText(getApplication(), R.string.save_success, Toast.LENGTH_SHORT).show();
                    } else if (checked) {
                        checked = false;
                        exist = false;
                        favoriteHelper.deleteTVShow(id_tvshow);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_unsaved));
                        startJob();
                        Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
                    } else {
                        exist = false;
                        favoriteHelper.deleteTVShow(id_tvshow);
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.fav_unsaved));
                        startJob();
                        Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<TVShow> call, Throwable t) {
                Log.e("Error", t.getMessage());
                Toast.makeText(DetailActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startJob() {
        int jobId = 100;
        int SCHEDULE_OF_PERIOD = 86000;

        ComponentName componentName = new ComponentName(this, UpdateWidgetService.class);

        JobInfo.Builder builder = new JobInfo.Builder(jobId, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setMinimumLatency(SCHEDULE_OF_PERIOD);
        } else {
            builder.setPeriodic(SCHEDULE_OF_PERIOD);
        }
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(builder.build());
    }
}