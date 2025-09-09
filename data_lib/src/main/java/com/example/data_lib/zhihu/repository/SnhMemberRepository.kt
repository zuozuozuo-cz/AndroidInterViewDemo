package com.example.data_lib.zhihu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.ApiResponse
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.NetworkBoundResource
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.dao.SnhDao
import com.example.data_lib.zhihu.entity.member.SnhMemberEntity
import com.example.data_lib.zhihu.entity.member.Team
import com.example.data_lib.zhihu.net.SnhMemberWebService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 获取SNH成员数据仓库层
 */
class SnhMemberRepository(
    private val memberDao: SnhDao,
    private val appExecutors: AppExecutors,
    private val netEngine: NetEngine
) {

    /**
     * 加载SNH成员数据
     */
    fun loadMembers(): LiveData<Resource<List<SnhMemberEntity>>> {
        return object :
            NetworkBoundResource<List<SnhMemberEntity>, List<SnhMemberEntity>>(appExecutors) {
            override fun loadFromDB(): LiveData<List<SnhMemberEntity>> {
                return memberDao.getAll()
            }

            override fun shouldFetch(data: List<SnhMemberEntity>?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<List<SnhMemberEntity>>> {
                val result = MediatorLiveData<ApiResponse<List<SnhMemberEntity>>>()
                appExecutors.netWorkIO.execute {
                    try {
                        netEngine.createService(SnhMemberWebService::class.java)
                            .getMemberList(Team.SNH48.gid).execute()
                    } catch (e: Exception) {
                        result.postValue(ApiResponse.error("网络错误:${e.message}", null))
                    }
                }
                return result
            }

            override fun saveCallResult(members: List<SnhMemberEntity>) {
                CoroutineScope(Dispatchers.IO).launch {
                    memberDao.insertAll(members)
                }
            }

        }.asLiveData()
    }

}