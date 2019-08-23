package com.cordy.secdra.module.pictureGal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cordy.secdra.module.main.view.MainActivity.Companion.adapter
import com.cordy.secdra.module.pictureGal.view.PicFragment

class VpPictureAdapter(support: FragmentManager)
    : FragmentPagerAdapter(support, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return PicFragment().newFragment(adapter?.data?.get(position))!!
    }

    override fun getCount(): Int {
        return adapter?.data?.size!!
    }
}