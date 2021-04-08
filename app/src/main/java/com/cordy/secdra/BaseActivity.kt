@file:Suppress("UNCHECKED_CAST")

package com.cordy.secdra

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import com.cordy.secdra.module.main.view.MainActivity
import com.cordy.secdra.module.search.view.SearchListActivity
import com.cordy.secdra.module.user.view.UserDetailsActivity
import com.cordy.secdra.utils.ActivityStackManager
import com.cordy.secdra.utils.ToastUtil
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var lastClickTime = 0L
    lateinit var vBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (this is SearchListActivity || this is UserDetailsActivity) {
            val config = SlidrConfig.Builder()
                .edge(true)
                .build()
            Slidr.attach(this, config)
        }
        ActivityStackManager.addToStack(this)
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        vBinding = method.invoke(null, layoutInflater) as VB
        setContentView(vBinding.root)
    }

    open fun initView() {
        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        if (this is MainActivity) {
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
        } else super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityStackManager.onDestroyRemove(this)
    }
}
