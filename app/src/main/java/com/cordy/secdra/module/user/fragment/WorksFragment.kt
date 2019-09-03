package com.cordy.secdra.module.user.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cordy.secdra.R
import kotlinx.android.synthetic.main.fragment_works_collect.view.*

@SuppressLint("InflateParams")
class WorksFragment : Fragment() {

    private lateinit var srlRefresh: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = layoutInflater.inflate(R.layout.fragment_works_collect, null)
        initView(rootView)
        return rootView
    }

    private fun initView(rootView: View) {
        srlRefresh = rootView.srl_refresh
    }
}