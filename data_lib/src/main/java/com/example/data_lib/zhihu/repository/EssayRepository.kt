package com.example.data_lib.zhihu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.ApiResponse
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.NetworkBoundResource
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.dao.ZhihuDao
import com.example.data_lib.zhihu.entity.essay.ZhihuItemEntity
import com.example.data_lib.zhihu.net.EssayWebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EssayRepository(
    private val zhihuDao: ZhihuDao,
    private val appExecutors: AppExecutors,
    private val netEngine: NetEngine
) {

    /**
     * 加载文章数据
     * 实现先本地后网络的数据获取策略
     *
     */
    fun loadEssays(): LiveData<Resource<ZhihuItemEntity>> {
        return object : NetworkBoundResource<ZhihuItemEntity, ZhihuItemEntity>(appExecutors) {
            override fun loadFromDB(): LiveData<ZhihuItemEntity> {
                /**
                 * ???
                 */
                return zhihuDao.loadLatestZhihuItem() as LiveData<ZhihuItemEntity>
            }

            override fun shouldFetch(data: ZhihuItemEntity?): Boolean {
                return data == null || isDataExpired(data)
            }


            override fun createCall(): LiveData<ApiResponse<ZhihuItemEntity>> {
                val result = MediatorLiveData<ApiResponse<ZhihuItemEntity>>()
                appExecutors.netWorkIO.execute {
                    try {
                        val response =
                            netEngine.createService(EssayWebService::class.java)
                                .getEssay("latest")
                                .execute()
                        result.postValue(ApiResponse.create(response))
                    } catch (e: Exception) {
                        result.postValue(ApiResponse.error("网络错误: ${e.message}", null))
                    }
                }
                return result
            }

            override fun saveCallResult(item: ZhihuItemEntity) {
                /**
                 * ToDo 这里需要优化，将线程池改成携程
                 */
                CoroutineScope(Dispatchers.IO).launch {
                    zhihuDao.saveZhihuItem(item)
                }

//                appExecutors.diskIO.execute {
//                    zhihuDao.clear()
//                    zhihuDao.saveZhihuItem(item)
//                }
            }

        }.asLiveData()
    }


    private fun isDataExpired(entity: ZhihuItemEntity): Boolean {
        // 数据过期策略
        return true
    }

}
//class EssayRepository(private val zhihuDao: ZhihuDao,
//                      private val appExecutors: AppExecutors) {
//
//    private val api: EssayWebService =
//        NetEngine.getInstance().createService(EssayWebService::class.java)
//
//    companion object{
//        @Volatile
//        private var instance:EssayRepository? = null
//        fun getInstance(dao: ZhihuDao,executors: AppExecutors):EssayRepository =
//            instance ?: EssayRepository(dao,executors).also {
//                instance = it
//            }
//    }
//
//    fun laodEssayData(): MediatorLiveData<Resource<ZhihuItemEntity>> {
//        val result = MediatorLiveData<Resource<ZhihuItemEntity>>()
//        // 先从缓存中取
//        val dbSource: LiveData<ZhihuItemEntity?> = zhihuDao.loadLatestZhihuItem()
//        result.addSource(dbSource, Observer { cached ->
//            result.value = Resource.loading(cached)
//        })
//        //通过网络刷新
//        appExecutors.netWorkIO.execute {
//            try {
//                kotlinx.coroutines.runBlocking {
//                    val response = api.getZhihuList("latest").execute()
//                    Log.e(Constant.COMMON_TAG, "response = $response")
//                    if (response.isSuccessful) {
//                        response.body()?.let { zhihuItemEntity ->
//                            zhihuDao.saveZhihuItem(zhihuItemEntity)
//                            result.postValue(Resource.success(zhihuItemEntity))
//                        } ?: run {
//                            result.postValue(Resource.error("返回数据为空"))
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                result.postValue(Resource.error(e.message ?: "网络异常"))
//            }
//
//        }
//
//        return result
//    }
//
//    fun update():MediatorLiveData<Resource<ZhihuItemEntity>>{
//        return laodEssayData()
//    }
//
//}