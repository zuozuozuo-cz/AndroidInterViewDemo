package com.example.base_lib.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 通用 ViewModel 工厂
 * @param creators 一个 Map 将 ViewModel 类映射到对应创建的 lambda
 *              key -> ViewModel 的 class 类型
 *              value -> 返回该ViewModel 实例的 lambda
 */
class BaseViewModelFactory(private val creators: Map<Class<out ViewModel>, () -> ViewModel>) :
    ViewModelProvider.Factory {

    /**
     * 创建 ViewModel 实例
     * @param modelClass 需要创建的View 类
     * @return 对应的ViewModel 实例
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // 先从 Map 中找
        val creator = creators[modelClass]
            // 如果没有，就找可以赋值的父类
            ?: creators.entries.firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            // 再没有就抛异常
            ?: throw IllegalArgumentException("未知的 ViewModel class $modelClass")
        // 执行创建
        return creator() as T
    }
}