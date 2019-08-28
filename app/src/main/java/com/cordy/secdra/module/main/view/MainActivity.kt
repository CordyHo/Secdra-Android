package com.cordy.secdra.module.main.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.adapter.PictureRvAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.module.main.model.MPictureModel
import com.cordy.secdra.module.pictureGal.view.PicGalleryActivity
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.utils.*
import com.cordy.secdra.widget.ImmersionBar
import com.cordy.secdra.widget.ScaleImageView
import com.google.gson.Gson
import com.zyyoona7.itemdecoration.provider.StaggeredGridItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("InflateParams")
class MainActivity : BaseActivity(), IPictureInterface, SwipeRefreshLayout.OnRefreshListener, RvItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private lateinit var dlDrawer: DrawerLayout
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView
    private val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    private val model = MPictureModel(this)
    private val adapter: PictureRvAdapter = PictureRvAdapter(this)
    private var page = 1   // 第一页为0，第二页为1
    private var lastClickTime: Long = 0
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var bundle: Bundle? = Bundle()   //接收元素共享View返回的位置，用于返回动画

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationTransparent)
        window.statusBarColor = ContextCompat.getColor(this, R.color.navigationTransparent)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        getDataFromUrl()
        setViewData()
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

    private fun getDataFromUrl() {   //请求网络
        model.getMainPictureFromUrl(0)
    }

    private fun setViewData() {    //设置用户信息
        val jsonBeanUser = Gson().fromJson(AccountManager.userDetails, JsonBeanUser::class.java)
        ImageLoader.setPortrait200FromUrl(jsonBeanUser.data?.head, iv_portrait)
    }

    private fun initBroadcastReceiver() {  //查看大图VP滑动时更新RV滑动
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                appbarLayout.setExpanded(false)
                intent?.run { rvPicture.scrollToPosition(intent.getIntExtra("scrollPos", 0)) }
            }
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter("scrollPos"))
    }

    override fun onItemClick(ivPicture: ImageView, pos: Int) {  //item点击事件
        bundle?.putInt("pos", pos)  //提前设置pos，防止第一次进入没有共享动画
        PicturesListMiddleware.setPictureList(adapter.data as ArrayList<JsonBeanPicture.DataBean.ContentBean>) //设置全局静态变量，用putExtra传递List给Activity的话，太大会炸掉
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, ivPicture, pos.toString())
        startActivity(Intent(this, PicGalleryActivity::class.java).putExtra("pos", pos), options.toBundle()) // pos 作为元素共享的唯一tag
    }

    override fun getPictureListSuccess(jsonBeanPicture: JsonBeanPicture, isLoadMore: Boolean) {
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
        stopRefresh()
        ToastUtil.showToastShort(msg)
    }

    override fun onLoadMoreRequested() {
        model.getMainPictureFromUrl(page)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.fb_top -> rvPicture.scrollToPosition(0)

            R.id.iv_portrait -> openDrawer()

            R.id.tv_search ->ToastUtil.showToastShort("去搜索！")
        }
    }

    override fun onRefresh() {
        getDataFromUrl()
    }

    private fun stopRefresh() {
        srlRefresh.post { srlRefresh.isRefreshing = false }
    }

    override fun initView() {
        dlDrawer = dl_drawer
        srlRefresh = srl_refresh
        rvPicture = rv_picture
        srlRefresh.setOnRefreshListener(this)
        iv_portrait.setOnClickListener(this)
        fb_top.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        srlRefresh.setColorSchemeResources(R.color.colorAccent)
        srlRefresh.setProgressViewOffset(true, 0, 100)
        srlRefresh.post { srlRefresh.isRefreshing = true }
        rvPicture.layoutManager = layoutManager
        rvPicture.adapter = adapter
        rvPicture.addItemDecoration(StaggeredGridItemDecoration(StaggeredGridItemDecoration.Builder().includeStartEdge().includeEdge().spacingSize(ScreenUtils.dp2px(this, 10f))))
        adapter.openLoadAnimation(ScaleInAnimation())
        adapter.setOnLoadMoreListener(this, rvPicture)
        adapter.setFooterView(layoutInflater.inflate(R.layout.rv_empty_footer, null))
        val navigationBarHeight = ScreenUtils.getNavigationBarHeight(this)
        adapter.footerLayout?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navigationBarHeight)  //设置RV底部=导航栏高度
        setToolBarHeightAndPadding()
        setFloatingActionButtonPadding(navigationBarHeight)
    }

    private fun setToolBarHeightAndPadding() {
        srlRefresh.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val statusBarHeight = ScreenUtils.getStatusHeight(this@MainActivity)
                val height = toolbar.measuredHeight
                toolbar.layoutParams.height = height + statusBarHeight
                toolbar.setPadding(0, statusBarHeight, 0, 0)
                if (height > 0)
                    srlRefresh.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setFloatingActionButtonPadding(navigationBarHeight: Int) {
        val param = fb_top.layoutParams as CoordinatorLayout.LayoutParams
        param.bottomMargin = param.bottomMargin + navigationBarHeight
        fb_top.layoutParams = param
    }

    private fun isDrawerOpen(): Boolean {
        return dlDrawer.isDrawerOpen(GravityCompat.START)
    }

    private fun openDrawer() {
        dlDrawer.openDrawer(GravityCompat.START)
    }

    private fun closeDrawer() {
        dlDrawer.closeDrawer(GravityCompat.START)
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        when {
            isDrawerOpen() -> closeDrawer()

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