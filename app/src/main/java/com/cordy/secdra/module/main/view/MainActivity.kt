package com.cordy.secdra.module.main.view

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R
import com.cordy.secdra.module.main.adapter.PictureRvAdapter
import com.cordy.secdra.module.main.bean.JsonBeanPicture
import com.cordy.secdra.module.main.dialog.LogoutDialog
import com.cordy.secdra.module.main.interfaces.IPictureInterface
import com.cordy.secdra.module.main.interfaces.RvItemClickListener
import com.cordy.secdra.module.main.model.MPictureModel
import com.cordy.secdra.module.pictureGal.view.PicGalleryActivity
import com.cordy.secdra.module.search.view.SearchListActivity
import com.cordy.secdra.module.user.bean.JsonBeanUser
import com.cordy.secdra.module.user.view.LoginActivity
import com.cordy.secdra.module.user.view.UserDetailsActivity
import com.cordy.secdra.utils.*
import com.cordy.secdra.widget.ImmersionBar
import com.cordy.secdra.widget.ScaleImageView
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.zyyoona7.itemdecoration.provider.StaggeredGridItemDecoration
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer_start.*
import kotlinx.android.synthetic.main.layout_navigation_view_header.view.*
import kotlinx.android.synthetic.main.view_float_button.*

@SuppressLint("InflateParams")
class MainActivity : BaseActivity(), IPictureInterface, SwipeRefreshLayout.OnRefreshListener, NavigationView.OnNavigationItemSelectedListener, RvItemClickListener,
        DrawerLayout.DrawerListener, OnLoadMoreListener, View.OnClickListener {

    private lateinit var dlDrawer: DrawerLayout
    private lateinit var nvNavigation: NavigationView
    private lateinit var ivPortraitDrawer: CircleImageView
    private lateinit var ivBackground: ImageView
    private lateinit var tvName: TextView
    private lateinit var srlRefresh: SwipeRefreshLayout
    private lateinit var rvPicture: RecyclerView
    private val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
    private val model = MPictureModel(this)
    private val adapter: PictureRvAdapter = PictureRvAdapter(this)
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager
    private var page = 1   // 第一页为0，第二页为1
    private var bundle: Bundle? = Bundle()   //接收元素共享View返回的位置，用于返回动画
    private var whichId = -1   //点击侧滑的菜单记录id，侧滑关闭后再根据id进行界面操作，提高体验
    private val tag = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        ImmersionBar(this).setImmersionBar()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navigationTransparent)
        window.statusBarColor = ContextCompat.getColor(this, R.color.navigationTransparent)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initNavigationView()
        getDataFromUrl()
        initBroadcastReceiver()
        exitShareElementCallback()
    }

    override fun onResume() {
        super.onResume()
        setViewData()
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
        if (!AccountManager.isSignIn) {
            nvNavigation.menu.findItem(R.id.action_logout_or_login).isVisible = false
            ImageLoader.setImageResource(R.drawable.ic_icon, iv_portrait)
            ImageLoader.setImageResource(R.drawable.ic_icon, ivPortraitDrawer)
            ImageLoader.setImageResource(R.drawable.ic_icon, ivBackground)
            tvName.text = getString(R.string.click_to_login)
        } else {
            nvNavigation.menu.findItem(R.id.action_logout_or_login).isVisible = true
            val jsonBeanUser = Gson().fromJson(AccountManager.userDetails, JsonBeanUser::class.java)
            ImageLoader.setPortraitFromUrl(jsonBeanUser.data?.head, iv_portrait)
            ImageLoader.setPortraitFromUrl(jsonBeanUser.data?.head, ivPortraitDrawer)
            ImageLoader.setBackGroundImageFromUrl(jsonBeanUser.data?.background, ivBackground)
            tvName.text = jsonBeanUser.data?.name
        }
    }

    private fun initBroadcastReceiver() {  //查看大图VP滑动时更新RV滑动
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.getStringExtra("tag") == tag) {
                    appbarLayout.setExpanded(false)
                    rvPicture.scrollToPosition(intent.getIntExtra("scrollPos", 0))
                }
            }
        }
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter("scrollPos"))
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
        if (!isLoadMore)   //第一次加载
            initRv(jsonBeanPicture)
        else
            loadMoreRv(jsonBeanPicture)  //加载更多
    }

    private fun initRv(jsonBeanPicture: JsonBeanPicture) {
        stopRefresh()
        page = 1
        adapter.setNewInstance(jsonBeanPicture.data.content)
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
        model.getMainPictureFromUrl(page)
    }

    override fun onClick(v: View) {
        when (v.id) {

            R.id.fb_top -> rvPicture.scrollToPosition(0)

            R.id.iv_portrait -> openDrawer()

            R.id.iv_menu -> openDrawer()

            R.id.tv_search -> startActivity(Intent(this, SearchListActivity::class.java))

            else -> setWhichId(v.id)    // else 的都是侧滑菜单里的id

        }
    }

    private fun setActionFromWhichId() {
        if (whichId != -1)
            when (whichId) {
                R.id.action_logout_or_login -> {
                    if (AccountManager.isSignIn)
                        LogoutDialog().show(this)
                }

                R.id.fl_header -> {
                    if (AccountManager.isSignIn)
                        startActivityForResult(Intent(this, UserDetailsActivity::class.java), 1)
                    else
                        startActivity(Intent(this, LoginActivity::class.java))
                }
            }
        whichId = -1
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {   //侧滑菜单点击事件
        setWhichId(item.itemId)
        return false
    }

    private fun setWhichId(id: Int) {   //记录侧滑点击view的id
        whichId = id
        closeDrawer()
    }

    override fun initView() {
        dlDrawer = dl_drawer
        nvNavigation = nv_navigation
        srlRefresh = srl_refresh
        rvPicture = rv_picture
        srlRefresh.setOnRefreshListener(this)
        dlDrawer.addDrawerListener(this)
        nvNavigation.setNavigationItemSelectedListener(this)
        iv_portrait.setOnClickListener(this)
        iv_menu.setOnClickListener(this)
        fb_top.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        srlRefresh.setColorSchemeResources(R.color.colorAccent)
        srlRefresh.setProgressViewOffset(true, 0, 100)
        srlRefresh.post { srlRefresh.isRefreshing = true }
        rvPicture.layoutManager = layoutManager
        rvPicture.adapter = adapter
        rvPicture.addItemDecoration(StaggeredGridItemDecoration(StaggeredGridItemDecoration.Builder().includeStartEdge().includeEdge().spacingSize(ScreenUtils.dp2px(this, 10f))))
        adapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        adapter.loadMoreModule.setOnLoadMoreListener(this)
        adapter.setFooterView(layoutInflater.inflate(R.layout.rv_empty_footer, null))
        val navigationBarHeight = ScreenUtils.getNavigationBarHeight(this)
        adapter.footerLayout?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, navigationBarHeight)  //设置RV底部=导航栏高度
        setToolBarMargin()
        setFloatingActionButtonPadding(navigationBarHeight)
    }

    private fun initNavigationView() {
        nvNavigation.getHeaderView(0).setOnClickListener(this)
        ivPortraitDrawer = nvNavigation.getHeaderView(0).iv_portraitDrawer
        ivBackground = nvNavigation.getHeaderView(0).iv_background
        tvName = nvNavigation.getHeaderView(0).tv_name
        nvNavigation.getHeaderView(0).lv_userInfo.setPadding(0, ScreenUtils.dp2px(this, 15f) + ScreenUtils.getStatusHeight(this), 0, 0)
    }

    private fun setToolBarMargin() {
        val statusBarHeight = ScreenUtils.getStatusHeight(this)
        (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).setMargins(0, statusBarHeight, 0, 0)
    }

    private fun setFloatingActionButtonPadding(navigationBarHeight: Int) {
        val param = fb_top.layoutParams as CoordinatorLayout.LayoutParams
        param.bottomMargin = param.bottomMargin + navigationBarHeight
        fb_top.layoutParams = param
    }

    override fun onDrawerClosed(drawerView: View) {  //侧滑关闭回调
        setActionFromWhichId()
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

    override fun onRefresh() {
        getDataFromUrl()
    }

    private fun stopRefresh() {
        srlRefresh.post { srlRefresh.isRefreshing = false }
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
    }

    override fun onBackPressed() {
        if (isDrawerOpen())
            closeDrawer()
        else
            super.onBackPressed()
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }
}