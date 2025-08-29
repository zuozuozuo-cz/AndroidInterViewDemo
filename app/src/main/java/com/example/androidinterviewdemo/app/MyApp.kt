package com.example.androidinterviewdemo.app

import android.app.Application
import com.alibaba.android.arouter.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.example.data_lib.zhihu.architecture.DbCallbackHelper
import com.example.base_lib.executors.AppExecutors
import com.example.data_lib.zhihu.AppDB
import com.example.data_lib.zhihu.EssayDbCallback

class MyApp : Application() {
    val appExecutors by lazy {
        AppExecutors()
    }

    val database by lazy {
        AppDB.getInstance(this,appExecutors)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        initDatabaseCallbacks()

    }

    private fun initDatabaseCallbacks() {
        DbCallbackHelper.registerCallbacks(EssayDbCallback())
    }


    // companion object  是kotlin的静态对象 相当于 Java 的static
    companion object {
        // 提供全局访问 AppExecutors 的方法
        // 延迟初始化的全局变量
        lateinit var instance: MyApp
            private set
    }

    init {
        // 每次创建MyApp对象的时候，将自己赋值给instance
        // 由于Application 只会创建一次，所以instance 实际上就是全局唯一的
        instance = this
    }


}

