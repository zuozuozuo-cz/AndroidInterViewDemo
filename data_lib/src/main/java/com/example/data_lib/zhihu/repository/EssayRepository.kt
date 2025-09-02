package com.example.data_lib.zhihu.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.base_lib.architecture.AbsRepository
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.dao.ZhihuDao
import com.example.data_lib.zhihu.entity.ZhihuItemEntity
import com.example.data_lib.zhihu.net.EssayWebService

class EssayRepository(private val zhihuDao: ZhihuDao,
                      private val appExecutors: AppExecutors) : AbsRepository() {

    private val api: EssayWebService =
        NetEngine.getInstance().createService(EssayWebService::class.java)

    companion object{
        @Volatile
        private var instance:EssayRepository? = null
        fun getInstance(dao: ZhihuDao,executors: AppExecutors):EssayRepository =
            instance ?: EssayRepository(dao,executors).also {
                instance = it
            }
    }

    fun laodEssayData(): MediatorLiveData<Resource<ZhihuItemEntity>> {
        val result = MediatorLiveData<Resource<ZhihuItemEntity>>()
        // 先从缓存中取
        val dbSource: LiveData<ZhihuItemEntity?> = zhihuDao.loadLatestZhihuItem()
        result.addSource(dbSource, Observer { cached ->
            result.value = Resource.loading(cached)
        })
        //通过网络刷新
        appExecutors.netWorkIO.execute {
            try {
                kotlinx.coroutines.runBlocking {
                    val response = api.getZhihuList("latest").execute()
                    Log.e(Constant.COMMON_TAG, "response = $response")
                    if (response.isSuccessful) {
                        response.body()?.let { zhihuItemEntity ->
                            zhihuDao.saveZhihuItem(zhihuItemEntity)
                            result.postValue(Resource.success(zhihuItemEntity))
                        } ?: run {
                            result.postValue(Resource.error("返回数据为空"))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                result.postValue(Resource.error(e.message ?: "网络异常"))
            }

        }

        return result
    }

    fun update():MediatorLiveData<Resource<ZhihuItemEntity>>{
        return laodEssayData()
    }

}