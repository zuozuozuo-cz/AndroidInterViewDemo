package com.example.base_lib.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.executors.AppExecutors

abstract class NetworkBoundResource<ResultType, RequestType>(private val appExecutors: AppExecutors) {

    //结果 LiveData
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        // 初始状态为加载中
        result.value = Resource.loading()

        // 先观察数据库
        val dbSource = loadFromDB()
        result.addSource(dbSource) { data ->
            if (shouldFetch(data)) {
                // 从网络获取
                fetchFromNetwork(dbSource)
            } else {
                // 使用数据库数据
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        // 先更新数据库数据为加载状态
//        result.addSource(dbSource) { newData ->
//            setValue(Resource.loading(newData))
//        }
        dbSource.value?.let { setValue(Resource.loading(it)) }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            when (response) {
                is ApiResponse.Success -> {
                    appExecutors.diskIO.execute {
                        // 保存网络结果到数据库
                        saveCallResult(response.body)
                        appExecutors.mainThread.execute {
                            // 重新监听数据库获取的最新数据
                            result.addSource(loadFromDB()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }

                is ApiResponse.Error -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    /**
     * 设置结果值
     */
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    /**
     * 类型转换
     */
    fun asLiveData(): LiveData<Resource<ResultType>> = result

    /**
     * 从数据库中获取数据
     */
    protected abstract fun loadFromDB(): LiveData<ResultType>

    /**
     * 判断是否需要从网络请求数据接口
     */
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    /**
     * 创建对应的call
     */
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    /**
     * 保存网络数据到数据库
     */
    protected abstract fun saveCallResult(item: RequestType)

    /**
     * 请求
     */
    protected open fun onFetchFailed() {}
}