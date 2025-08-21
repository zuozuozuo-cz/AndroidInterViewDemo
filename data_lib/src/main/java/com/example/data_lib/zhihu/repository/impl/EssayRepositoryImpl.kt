package com.example.data_lib.zhihu.repository.impl

import androidx.lifecycle.LiveData
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.dao.EssayDao
import com.example.data_lib.zhihu.dao.ZhihuDao
import com.example.data_lib.zhihu.entity.EssayDayEntity
import com.example.data_lib.zhihu.entity.ZhihuItemEntity
import com.example.data_lib.zhihu.net.EssayWebService
import com.example.data_lib.zhihu.repository.interfaces.EssayRepository

class EssayRepositoryImpl @Inject constructor(
    private val netEngine: NetEngine,
    private val zhihuDao: ZhihuDao,
    private val essayDao: EssayDao,
    private val appExecutors: AppExecutors
) : EssayRepository{


    private val essayWebService:EssayWebService by lazy {

    }


    override fun getEssay(type: String): LiveData<Resource<ZhihuItemEntity>> {
        TODO("Not yet implemented")
    }

    override fun getAllEssays(): LiveData<Resource<EssayDayEntity>> {
        TODO("Not yet implemented")
    }

    override fun getEssayById(id: Int): LiveData<Resource<EssayDayEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveEssay(essay: EssayDayEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun refreshEssay() {
        TODO("Not yet implemented")
    }

}