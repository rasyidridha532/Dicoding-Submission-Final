package com.example.favorite.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class FavPagerAdapter(fm: FragmentManager, private val map: Map<String, Fragment>) :
        FragmentStatePagerAdapter(fm) {

    private val titles = map.keys.toList()
    private val fragment = map.values.toList()

    override fun getItem(position: Int): Fragment = fragment[position]

    override fun getCount(): Int = map.size

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}
