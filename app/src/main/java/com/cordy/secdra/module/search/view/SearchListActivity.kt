package com.cordy.secdra.module.search.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.databinding.ActivitySearchListBinding
import com.cordy.secdra.module.main.adapter.PictureRvAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.module.pictureGal.view.PicGalleryActivity
import com.cordy.secdra.module.search.model.MPictureSearchModel
import com.cordy.secdra.utils.InputMethodUtils
import com.cordy.secdra.utils.PicturesListMiddleware
import com.cordy.secdra.utils.ScreenUtils
import com.cordy.secdra.utils.ToastUtil
import com.cordy.secdra.widget.ScaleImageView
import com.zyyoona7.itemdecoration.provider.StaggeredGridItemDecoration

class SearchListActivity : BaseActivity<ActivitySearchListBinding>(), TextView.OnEditorActionListener, IPictureInterface, SwipeRefreshLayout.OnRefreshListener, RvItemClickListener,
        OnLoadMoreListener, View.OnClickListener {

    private var content: String? = ""  //搜索的内容，因为有翻页，所以要保存输入的内容来请求
    private val model = MPictureSearchModel(this)
    private lateinit var etSearch: EditText
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView
    private val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    private val adapter: PictureRvAdapter = PictureRvAdapter(this)
    private lateinit var broadcastReceiver: BroadcastReceiver
    private var page = 1
    private var bundle: Bundle? = Bundle()   //接收元素共享View返回的位置，用于返回动画
    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initBroadcastReceiver()
        exitShareElementCallback()
    }

    override fun onActivityReenter(resultCode: Int, data: Intent) {
        super.onActivityReenter(resultCode, data)
        bundle = Bundle(data.extras)
    }

    private fun exitShareElementCallback() {    //返回时元素共享处理
        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String?, View>?) {
                bundle?.run {
                    val pos = bundle?.getInt("pos", 0)
                    sharedElements?.clear()
                    names?.clear()
                    val itemView = pos?.let { layoutManager.findViewByPosition(it) }
                    itemView?.run {
                        val imageView = itemView.findViewById<ScaleImageView>(R.id.iv_picture) as View
                        sharedElements?.set(pos.toString(), imageView)  // pos 作为元素共享的唯一tag
                    }
                    bundle = null
                }
            }
        })
    }

    private fun initBroadcastReceiver() {  //查看大图VP滑动时更新RV滑动
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.getStringExtra("tag") == tag) {
                    vBinding.appbarLayout.setExpanded(false)
                    rvPicture.scrollToPosition(intent.getIntExtra("scrollPos", 0))
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("scrollPos"))
    }

    override fun onItemClick(ivPicture: ImageView, pos: Int) {  //item点击事件
        bundle?.putInt("pos", pos)  //提前设置pos，防止第一次进入没有共享动画
        PicturesListMiddleware.setPictureList(adapter.data as ArrayList<JsonBeanPicture.DataBean.ContentBean>) //设置全局静态变量，用putExtra传递List给Activity的话，太大会炸掉
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ivPicture, pos.toString())
        startActivity(Intent(this, PicGalleryActivity::class.java)
                .putExtra("pos", pos)
                .putExtra("tag", tag), options.toBundle()) // pos 作为元素共享的唯一tag，tag用于接收滚动RV广播
    }

    override fun getPictureListSuccess(jsonBeanPicture: JsonBeanPicture, isLoadMore: Boolean) {
        rvPicture.visibility = View.VISIBLE
        hideKeyboard()
        stopRefresh()
        if (!isLoadMore)   //第一次加载
            initRv(jsonBeanPicture)
        else
            loadMoreRv(jsonBeanPicture)  //加载更多
    }

    private fun initRv(jsonBeanPicture: JsonBeanPicture) {
        if (jsonBeanPicture.data.content.isNotEmpty()) {
            page = 1
            rvPicture.scrollToPosition(0)  // 得到新数据后不会回到顶部，要调用滚到顶部
            adapter.setNewInstance(jsonBeanPicture.data.content)
        } else
            rvPicture.visibility = View.INVISIBLE
    }

    private fun loadMoreRv(jsonBeanPicture: JsonBeanPicture) {
        if (jsonBeanPicture.data.content.isNotEmpty()) {
            adapter.loadMoreModule.loadMoreComplete()
            adapter.addData(jsonBeanPicture.data.content)
            page++
        } else
            adapter.loadMoreModule.loadMoreEnd(true)
    }

    override fun getPictureListFailure(msg: String?) {
        if (rvPicture.childCount <= 0)
            rvPicture.visibility = View.INVISIBLE
        stopRefresh()
        ToastUtil.showToastShort(msg)
    }

    override fun onLoadMore() {
        model.searchPictureFromUrl(content, page)
    }

    private fun searchDataFromUrl() {   // 搜索的请求方法
        if (!etSearch.text.isNullOrBlank()) {
            srlRefresh.post { srlRefresh.isRefreshing = true }
            content = etSearch.text.toString().trim()
            model.searchPictureFromUrl(content, 0)
        } else
            ToastUtil.showToastShort(getString(R.string.please_input))
    }

    private fun useContentGetDataFromUrl() {   // 刷新的请求方法
        if (!content.isNullOrBlank())
            model.searchPictureFromUrl(content, 0)
        else
            stopRefresh()
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        searchDataFromUrl()
        return true
    }

    override fun onRefresh() {
        useContentGetDataFromUrl()
    }

    private fun hideKeyboard() {
        InputMethodUtils.closeInputMethod(etSearch)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fb_top -> rvPicture.scrollToPosition(0)
        }
    }

    private fun stopRefresh() {
        srlRefresh.post { srlRefresh.isRefreshing = false }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    override fun initView() {
        super.initView()
        etSearch = vBinding.etSearch
        srlRefresh = vBinding.srlRefresh
        rvPicture = vBinding.rvPicture
        etSearch.requestFocus()
        etSearch.setOnEditorActionListener(this)
        srlRefresh.setOnRefreshListener(this)
        srlRefresh.setColorSchemeResources(R.color.colorAccent)
        srlRefresh.setProgressViewOffset(true, 0, 100)
        rvPicture.layoutManager = layoutManager
        rvPicture.adapter = adapter
        rvPicture.addItemDecoration(StaggeredGridItemDecoration(StaggeredGridItemDecoration.Builder().includeStartEdge().includeEdge().spacingSize(ScreenUtils.dp2px(this, 10f))))
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        adapter.loadMoreModule.setOnLoadMoreListener(this)
        rvPicture.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    hideKeyboard()
            }
        })
        vBinding.viewFloatButton.fbTop.setOnClickListener(this)
    }
}