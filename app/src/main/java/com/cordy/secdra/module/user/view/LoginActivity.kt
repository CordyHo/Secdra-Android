package com.cordy.secdra.module.user.view

import android.os.Bundle
import com.cordy.secdra.BaseActivity
import com.cordy.secdra.R

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }
}