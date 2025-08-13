package com.example.androidinterviewdemo.app

import android.app.Application
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

class MyApp :Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }
}