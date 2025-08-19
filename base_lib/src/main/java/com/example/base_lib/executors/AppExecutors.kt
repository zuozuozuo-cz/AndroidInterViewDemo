package com.example.base_lib.executors

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors (
    // 磁盘 IO 线程，默认单线程执行任务
    val diskIO: Executor = Executors.newSingleThreadExecutor(),
    // 网络 IO 线程，默认规定线程池 3个线程
    val  netWorkIO: Executor = Executors.newFixedThreadPool(3),
    // 主线程 executor 用于在 UI线程中执行任务
    val mainThread: Executor = MainThreadExecutor()
){


    private  class MainThreadExecutor: Executor{
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(p0: Runnable) {
            mainThreadHandler.post(p0);
        }
    }

}