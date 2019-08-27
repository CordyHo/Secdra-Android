package com.cordy.secdra.module.pictureGal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.pictureGal.view.PicFragment

class VpPictureAdapter(support: FragmentManager, private val pictureList: ArrayList<JsonBeanPicture.DataBean.ContentBean>?)
    : FragmentPagerAdapter(support, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(pos: Int): Fragment {
        return PicFragment().newFragment(pictureList?.get(pos))
    }

    override fun getCount(): Int {
        return pictureList?.size!!
    }
}