package com.example.data_lib.member.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.ApiResponse
import com.example.base_lib.net.NetConstant
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.NetworkBoundResource
import com.example.base_lib.net.Resource
import com.example.data_lib.member.api.SnhMemberWebService
import com.example.data_lib.member.dao.SnhDao
import com.example.data_lib.member.entity.MemberResponse
import com.example.data_lib.member.entity.SnhMemberEntity
import com.example.data_lib.member.entity.Team
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
    fun loadMembers(team: Team): LiveData<Resource<List<SnhMemberEntity>>> {
        return object : NetworkBoundResource<List<SnhMemberEntity>, MemberResponse>(appExecutors) {
            override fun loadFromDB(): LiveData<List<SnhMemberEntity>> {
                val all = memberDao.getAll()
                all.observeForever { list ->
                    Log.e(
                        Constant.COMMON_TAG,
                        "SnhMemberRepository ======== loadFromDB ======== size:${list?.size}"
                    )
                }
                return all
            }

            override fun shouldFetch(data: List<SnhMemberEntity>?): Boolean {
                val bool = data?.isEmpty() ?: true
                Log.e(
                    Constant.COMMON_TAG,
                    "SnhMemberRepository ======== shouldFetch ========bool:${bool}"
                )
                return bool
            }

            override fun createCall(): LiveData<ApiResponse<MemberResponse>> {
                val result = MediatorLiveData<ApiResponse<MemberResponse>>()
                appExecutors.netWorkIO.execute {
                    try {
                        netEngine.setBaseUrl(NetConstant.URL_SNH_BASE)
                        val response = netEngine.createService(SnhMemberWebService::class.java)
                            .getMemberList(team.gid).execute()
                        Log.e(
                            Constant.COMMON_TAG,
                            "SnhMemberRepository ======== createCall ========response:${response}"
                        )
                        result.postValue(ApiResponse.Companion.create(response))
                    } catch (e: Exception) {
                        Log.e(
                            Constant.COMMON_TAG,
                            "SnhMemberRepository ======== createCall Exception ========message:${e.message}"
                        )
                        result.postValue(ApiResponse.Companion.error("网络错误:${e.message}", null))
                    }
                }
                return result
            }

            override fun saveCallResult(members: MemberResponse) {
                CoroutineScope(Dispatchers.IO).launch {
                    Log.e(
                        Constant.COMMON_TAG,
                        "SnhMemberRepository ======== saveCallResult ========members:${members.rows.size}"
                    )
                    try {
                        memberDao.insertAll(members.rows)
                        Log.e(
                            Constant.COMMON_TAG,
                            "SnhMemberRepository ======== insertAll success========"
                        )
                    } catch (e: Error) {
                        Log.e(
                            Constant.COMMON_TAG,
                            "SnhMemberRepository ======== insertAll Error========:${e.message}"
                        )
                    }
                }
            }

        }.asLiveData()

    }

}