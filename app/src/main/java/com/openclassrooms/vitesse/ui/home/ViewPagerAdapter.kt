package com.openclassrooms.vitesse.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllItemsFragment()
            1 -> FavoriteFragment()
            else -> AllItemsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // Total numbers of tabs
    }
}
