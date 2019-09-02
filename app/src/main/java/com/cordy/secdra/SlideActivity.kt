package com.cordy.secdra

import android.os.Bundle
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig

abstract class SlideActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = SlidrConfig.Builder()
            .edge(true)
            .build()
        Slidr.attach(this, config)
    }
}

