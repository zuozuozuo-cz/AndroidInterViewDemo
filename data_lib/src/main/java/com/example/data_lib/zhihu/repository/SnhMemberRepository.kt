package com.example.data_lib.zhihu.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.architecture.AbsRepository
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.dao.SnhDao
import com.example.data_lib.zhihu.entity.member.SnhMemberEntity
import com.example.data_lib.zhihu.entity.member.Team
import com.example.data_lib.zhihu.net.SnhMemberWebService
import kotlinx.coroutines.runBlocking

/**
 * 获取SNH成员数据仓库层
 */
class SnhMemberRepository(private val memberDao: SnhDao, private val appExecutors: AppExecutors) :
    AbsRepository() {

    private val api: SnhMemberWebService =
        NetEngine.getInstance().createService(SnhMemberWebService::class.java)

    companion object {
        @Volatile
        private var instance: SnhMemberRepository? = null
        fun getInstance(dao: SnhDao, executors: AppExecutors): SnhMemberRepository =
            instance ?: SnhMemberRepository(dao, executors).also {
                instance = it
            }
    }

    suspend fun loadMemberData(): MediatorLiveData<Resource<List<SnhMemberEntity>>> {
        // 同时观察多个 LiveData 的 LiveData
        val result = MediatorLiveData<Resource<List<SnhMemberEntity>>>()
        // 先从缓存中取
        val dbSource: LiveData<List<SnhMemberEntity>> = memberDao.getAll()
        /**
         * @param source 需要观察的数据源
         * @param onChanged 数据发生改变的时候触发的回调
         * 这里先将缓存添加给 result 进行监听，当数据发生变化的时候触发Loading
         */
        result.addSource(dbSource, { cache ->
            result.value = Resource.loading(cache)
        })

        appExecutors.netWorkIO.execute {
            try {
                runBlocking {
                    NetEngine.getInstance().setBaseUrl("https://h5.48.cn/resource/jsonp/")
                    val response = api.getMemberList(Team.SNH48.gid).execute()
                    Log.e(Constant.COMMON_TAG, "response = $response")
                    if (response.isSuccessful) {
                        response.body()?.let { snhMemberEntities ->
                            memberDao.insertAll(snhMemberEntities)
                            result.postValue(Resource.success(snhMemberEntities))
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

}