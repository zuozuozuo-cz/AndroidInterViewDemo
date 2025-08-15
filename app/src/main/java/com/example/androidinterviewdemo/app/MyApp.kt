package com.example.androidinterviewdemo.app

import android.app.Application
import com.example.base_lib.executors.AppExecutors

class MyApp : Application() {

    lateinit var appExecutors: AppExecutors
        private set // 只允许MyApp 内部赋值

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)

        appExecutors = AppExecutors()
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

