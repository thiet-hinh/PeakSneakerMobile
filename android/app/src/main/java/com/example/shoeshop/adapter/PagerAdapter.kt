package com.example.shoeshop.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shoeshop.fragment.OrderFragment
import com.example.shoeshop.fragment.PageFragment
import com.example.shoeshop.fragment.ProfileFragment

class PagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 4
    override fun createFragment(position: Int) = when (position) {
        0 -> PageFragment.newInstance("HOME")
        1 -> PageFragment.newInstance("STORE")
        2 -> OrderFragment()
        else -> ProfileFragment()
    }
}