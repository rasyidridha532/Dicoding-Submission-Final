package com.example.favorite.main

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.favorite.R
import com.example.favorite.adapter.FavPagerAdapter
import com.example.favorite.fragment.FavoriteMovieFragment
import com.example.favorite.fragment.FavoriteTVShowFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val viewPager: ViewPager = findViewById(R.id.view_pager)
        view_pager.adapter = FavPagerAdapter(
                supportFragmentManager,
                mapOf(getString(R.string.movie) to FavoriteMovieFragment(),
                        getString(R.string.tvshow) to FavoriteTVShowFragment()
                )
        )
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}