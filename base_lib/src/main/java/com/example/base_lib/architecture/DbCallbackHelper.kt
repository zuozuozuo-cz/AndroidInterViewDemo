package com.example.base_lib.architecture

/**
 * 数据库回调辅助类
 * 用于集中管理数据库的创建和升级逻辑
 */
object DbCallbackHelper {

    // 保存所有数据库回调对象
    private val mDbCallbacks: ArrayList<AbsDbCallback> = ArrayList()

    /**
     * 初始化数据化回调
     * 在这里可以注册需要的回调
     */
    fun init(){

    }
}