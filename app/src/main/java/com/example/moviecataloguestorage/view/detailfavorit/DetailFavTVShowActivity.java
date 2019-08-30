package com.example.moviecataloguestorage.view.detailfavorit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.moviecataloguestorage.database.FavoriteHelper;
import com.example.moviecataloguestorage.view.MainActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviecataloguestorage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFavTVShowActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_detail)
    TextView tvTitle;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.tv_synopsis)
    TextView tvDesc;

    @BindView(R.id.img_poster_detail)
    ImageView imgPoster;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    String id_tvshow, judul, rating, linkPoster, deskripsi;

    FavoriteHelper favoriteHelper;

    boolean exist;
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_fav_tvshow);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        id_tvshow = getIntent().getStringExtra("id_tvshow");
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

        addTVShowFavorite();
        progressBar.setVisibility(View.GONE);
    }

    public void addTVShowFavorite() {
        favoriteHelper = new FavoriteHelper(getApplicationContext());
        favoriteHelper.open();
        exist = favoriteHelper.checkIsTVShowAlreadyFavorited(id_tvshow);
        if (exist) {
            fab.setImageResource(R.drawable.fav_saved);
        }

        fab.setOnClickListener(view -> deleteDialog());
    }

    public void deleteDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(R.string.delete_fav_sure);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", (dialog, which) -> {
            if (checked) {
                checked = false;
                exist = false;

                favoriteHelper.deleteTVShow(id_tvshow);
                fab.setImageResource(R.drawable.fav_unsaved);

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
            } else {
                exist = false;

                favoriteHelper.deleteTVShow(id_tvshow);
                fab.setImageResource(R.drawable.fav_unsaved);

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Toast.makeText(getApplication(), R.string.delete_save, Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", (dialog, which) -> alertDialog.dismiss());

        alertDialog.show();
    }
}
