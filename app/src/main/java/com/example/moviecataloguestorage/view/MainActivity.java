package com.example.moviecataloguestorage.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.example.moviecataloguestorage.R;
import com.example.moviecataloguestorage.view.search.SearchMovieActivity;
import com.example.moviecataloguestorage.view.search.SearchTVShowActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.moviecataloguestorage.fragment.FavoriteFragment;
import com.example.moviecataloguestorage.fragment.MovieFragment;
import com.example.moviecataloguestorage.fragment.TVShowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_movies);

            if (actionBar != null) {
                actionBar.setTitle(R.string.movie);
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment;
        ActionBar actionBar = getSupportActionBar();

        switch (item.getItemId()) {
            case R.id.navigation_movies:
                fragment = new MovieFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment,
                        fragment.getClass().getSimpleName()).commit();

                if (actionBar != null) {
                    actionBar.setTitle(R.string.movie);
                }
                return true;
            case R.id.navigation_tv_show:
                fragment = new TVShowFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment,
                        fragment.getClass().getSimpleName()).commit();

                if (actionBar != null) {
                    actionBar.setTitle(R.string.tv_show);
                }
                return true;
            case R.id.navigation_favorite:
                fragment = new FavoriteFragment();

                getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment,
                        fragment.getClass().getSimpleName()).commit();

                if (actionBar != null) {
                    actionBar.setTitle(R.string.favorite);
                }
                return true;
        }
        return false;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.searchdialog:
                dialogSearch();
                break;
            case R.id.action_change_settings:
                Intent mIntent = new Intent(this, SettingActivity.class);
                startActivity(mIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dialogSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setCancelable(true);
        builder.setTitle(R.string.search_dialog);

        builder.setNegativeButton(R.string.movie, (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, SearchMovieActivity.class);
            startActivity(intent);

            dialog.dismiss();
        });

        builder.setPositiveButton(R.string.tv_show, (dialog, which) -> {
            Intent intent = new Intent(MainActivity.this, SearchTVShowActivity.class);
            startActivity(intent);

            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}