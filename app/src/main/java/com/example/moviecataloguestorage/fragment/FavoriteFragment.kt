package com.example.moviecataloguestorage.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.moviecataloguestorage.R
import com.example.moviecataloguestorage.adapter.FavPagerAdapter
import com.example.moviecataloguestorage.model.Movie
import com.example.moviecataloguestorage.model.TVShow
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_favorite.*

import java.util.ArrayList

class FavoriteFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_favorite, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(activity as AppCompatActivity) {

            view_pager.adapter = FavPagerAdapter(
                    supportFragmentManager,
                    mapOf(getString(R.string.movie) to FavMovieFragment(),
                            getString(R.string.tv_show) to FavTVShowFragment()
                    )
            )
            tabs.setupWithViewPager(view_pager)
        }
    }
}