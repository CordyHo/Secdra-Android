package com.cordy.secdra.module.search.view

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.adapter.PictureRvAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.module.search.model.MPictureSearchModel
import com.cordy.secdra.utils.ScreenUtils
import com.cordy.secdra.utils.ToastUtil
import com.zyyoona7.itemdecoration.provider.StaggeredGridItemDecoration
import kotlinx.android.synthetic.main.activity_search_list.*

class SearchListActivity : BaseActivity(), TextView.OnEditorActionListener, IPictureInterface, SwipeRefreshLayout.OnRefreshListener, RvItemClickListener
        , BaseQuickAdapter.RequestLoadMoreListener {

    private var content = ""  //搜索的内容，因为有翻页，所以要保存输入的内容来请求
    private val model = MPictureSearchModel(this)
    private lateinit var etSearch: EditText
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView
    private val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    private val adapter: PictureRvAdapter = PictureRvAdapter(this)
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_list)
        initView()
    }

    private fun getDataFromUrl() {
        if (!content.isBlank())
            model.searchPictureFromUrl(content, 0)
        else if (!etSearch.text.isNullOrBlank()) {
            srlRefresh.post { srlRefresh.isRefreshing = true }
            content = etSearch.text.toString().trim()
            model.searchPictureFromUrl(content, 0)
        } else
            ToastUtil.showToastShort(getString(R.string.please_input))
    }

    override fun onItemClick(ivPicture: ImageView, pos: Int) {
    }

    override fun getPictureListSuccess(jsonBeanPicture: JsonBeanPicture, isLoadMore: Boolean) {
        rvPicture.visibility = View.VISIBLE
        if (!isLoadMore)   //第一次加载
            initRv(jsonBeanPicture)
        else
            loadMoreRv(jsonBeanPicture)  //加载更多
    }

    private fun initRv(jsonBeanPicture: JsonBeanPicture) {
        stopRefresh()
        page = 1
        adapter.setNewData(jsonBeanPicture.data.content)
    }

    private fun loadMoreRv(jsonBeanPicture: JsonBeanPicture) {
        if (jsonBeanPicture.data.content.isNotEmpty()) {
            adapter.loadMoreComplete()
            adapter.addData(jsonBeanPicture.data.content)
            page++
        } else
            adapter.loadMoreEnd(true)
    }

    override fun getPictureListFailure(msg: String?) {
        if (rvPicture.childCount <= 0)
            rvPicture.visibility = View.INVISIBLE
        stopRefresh()
        ToastUtil.showToastShort(msg)
    }

    override fun onLoadMoreRequested() {
        model.searchPictureFromUrl(content, page)
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        getDataFromUrl()
        return true
    }

    override fun onRefresh() {
        getDataFromUrl()
    }

    private fun stopRefresh() {
        srlRefresh.post { srlRefresh.isRefreshing = false }
    }

    override fun initView() {
        super.initView()
        etSearch = et_search
        etSearch.requestFocus()
        etSearch.setOnEditorActionListener(this)
        srlRefresh = srl_refresh
        srlRefresh.setOnRefreshListener(this)
        srlRefresh.setColorSchemeResources(R.color.colorAccent)
        srlRefresh.setProgressViewOffset(true, 0, 100)
        srlRefresh.post { srlRefresh.isRefreshing = true }
        rvPicture.layoutManager = layoutManager
        rvPicture.adapter = adapter
        rvPicture.addItemDecoration(StaggeredGridItemDecoration(StaggeredGridItemDecoration.Builder().includeStartEdge().includeEdge().spacingSize(ScreenUtils.dp2px(this, 10f))))
    }
}