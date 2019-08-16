package com.cordy.secdra.module.main.view

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.adapter.PictureRvAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.module.main.model.MPictureModel
import com.cordy.secdra.utils.ScreenUtils
import com.cordy.secdra.utils.ToastUtil
import com.cordy.secdra.widget.GridLayoutItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), IPictureInterface, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private var lastClickTime: Long = 0
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView
    private val adapter = PictureRvAdapter()
    private val model = MPictureModel(this)
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun getDataFromUrl() {
        model.getMainPictureFromUrl(0)
    }

    override fun getMorePictureListSuccess(jsonBeanPicture: JsonBeanPicture) {
    }

    override fun getMorePictureListFailure(msg: String?) {
    }

    override fun getPictureListSuccess(jsonBeanPicture: JsonBeanPicture) {
    }

    override fun getPictureListFailure(msg: String?) {
    }

    override fun onLoadMoreRequested() {
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

    override fun onRefresh() {
        getDataFromUrl()
    }

    override fun initView() {
        srlRefresh = srl_refresh
        rvPicture = rv_picture
        srlRefresh.setColorSchemeResources(R.color.colorAccent)
        srlRefresh.setProgressViewOffset(true, 0, 100)
        srlRefresh.post { srlRefresh.isRefreshing = true }
        rvPicture.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        rvPicture.addItemDecoration(GridLayoutItemDecoration(ScreenUtils.dp2px(this, 10f), true))
        rvPicture.adapter = adapter
        adapter.setOnLoadMoreListener(this, rvPicture)
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        when {
            lastClickTime <= 0 -> {
                lastClickTime = currentTime
                ToastUtil.showToastShort(getString(R.string.press_once_again_exit) + getString(R.string.app_name))
            }
            currentTime - lastClickTime < 1500 -> super.onBackPressed()
            else -> {
                ToastUtil.showToastShort(getString(R.string.press_once_again_exit) + getString(R.string.app_name))
                lastClickTime = currentTime
            }
        }
    }
}