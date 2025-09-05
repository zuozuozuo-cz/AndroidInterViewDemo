package com.example.base_lib.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.Resource
import kotlinx.coroutines.runBlocking

abstract class AbsRepository<T> : BaseRepository<T> {

    // 通过抽象属性获取依赖，由子类实现
    protected abstract val dao: Any
    protected abstract val appExecutors: AppExecutors
    protected abstract val api: Any


    override fun loadData(): MediatorLiveData<Resource<T>> {
        val result = MediatorLiveData<Resource<T>>()

        //先从数据库加载
        val dbSource = loadFromDB()

        result.addSource(dbSource) { data ->
            //判断是否需要从网络获取数据
            if (shouldFetch(data)) {
                // 从网络获取数据
                fetchFromNetwork(result, dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    result.value = Resource.success(newData)

                }
            }

        }
        return result
    }

    private fun fetchFromNetwork(result: MediatorLiveData<Resource<T>>, dbSource: LiveData<T>?) {
        // 先移除数据源的监听
        dbSource?.let {
            result.removeSource(it)
        }

        appExecutors.netWorkIO.execute {
            try {
                runBlocking {
                    val response = loadFromNetwork()
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            // 保存到数据库
                            saveCallResult(data)
                            // 重新从数据库加载最新数据
                            appExecutors.mainThread.execute {
                                result.addSource(loadFromDB()) { newData ->
                                    result.value = Resource.success(newData)
                                }
                            }
                        } ?: run {
                            // 数据为空
                            appExecutors.mainThread.execute {
                                result.value = Resource.error("返回数据为空")
                            }
                        }
                    } else {
                        // 请求失败
                        appExecutors.mainThread.execute {
                            result.value = Resource.error("请求失败:${response.code()}")
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                appExecutors.mainThread.execute {
                    result.value = Resource.error(e.message ?: "网络异常")
                }
            }
        }
    }

    /**
     * 默认实现，总是从网络获取
     */
    override fun shouldFetch(data: T): Boolean = true
}