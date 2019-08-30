package com.example.favorite.detail;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.favorite.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailMovieActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_detail)
    TextView tvTitle;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_synopsis)
    TextView tvDesc;

    @BindView(R.id.img_poster_detail)
    ImageView imgPoster;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String id_movie, judul, rating, linkPoster, deskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        id_movie = getIntent().getStringExtra("id_movie");
        judul = getIntent().getStringExtra("judul");
        rating = getIntent().getStringExtra("rating");
        linkPoster = getIntent().getStringExtra("link_poster");
        deskripsi = getIntent().getStringExtra("deskripsi");

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.toolbar_layout);

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
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

        tvTitle.setText(judul);
        tvRating.setText("Rating: " + rating);
        tvDesc.setText(deskripsi);

        Glide.with(getApplicationContext())
                .load("http://image.tmdb.org/t/p/w500" + linkPoster)
                .into(imgPoster);
    }
}