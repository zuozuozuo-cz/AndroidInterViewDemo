package com.example.kotlin_study

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

fun main() {

    // 启动协程
    val coroutineScope = CoroutineScope(context = EmptyCoroutineContext)
    coroutineScope.launch {

    }

    // 主线程
    val scope1 = CoroutineScope(Dispatchers.Main)
    // IO 密集型
    val scope2 = CoroutineScope(Dispatchers.IO)
    // 计算密集型
    val scope3 = CoroutineScope(Dispatchers.Default)
    // 无限制，不切线程
    val scope4 = CoroutineScope(Dispatchers.Unconfined)

    // 自定义的线程池，数量 + 名字
    val context = newFixedThreadPoolContext(22, "MyThread")
    CoroutineScope(context)
    context.close()
    // 单线程的线程池
    newSingleThreadContext("MyThread2")


}