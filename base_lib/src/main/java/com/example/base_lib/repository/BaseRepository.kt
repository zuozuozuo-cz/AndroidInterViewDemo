package com.example.base_lib.repository

import androidx.lifecycle.LiveData
import com.example.base_lib.net.Resource
import retrofit2.Response

/**
 * 仓库基础层接口
 * 定义所有仓库的基本操作
 */
interface BaseRepository<T> {
    /**
     * 加载数据
     * @return 包含数据资料的liveData
     */
    fun loadData(): LiveData<Resource<T>>


    /**
     * 刷新数据
     * @return 包含数据资料的liveData
     */
    fun refresh(): LiveData<Resource<T>>

    /**
     * 从数据库中加载
     * @return 包含数据资料的liveData
     */
    fun loadFromDB(): LiveData<T>


    /**
     * 从网络加载数据
     * @return 网络请求的响应
     */
    suspend fun loadFromNetwork(): Response<T>

    /**
     * 保存网络结果到数据库
     * @param item 网络获取的数据
     */
    suspend fun saveCallResult(item: T)

    /**
     * 判断是否需要从网络获取数据
     * @param data 本地数据
     * @return 是否需要从网络获取
     */
    fun shouldFetch(data: T): Boolean
}