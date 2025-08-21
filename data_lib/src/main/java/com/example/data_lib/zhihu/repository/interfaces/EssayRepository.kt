package com.example.data_lib.zhihu.repository.interfaces

import androidx.lifecycle.LiveData
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.entity.EssayDayEntity
import com.example.data_lib.zhihu.entity.ZhihuItemEntity

interface EssayRepository {
    /**
     * 获取知乎文章数据
     * @param type 文章类型
     * @return 包含文章数据的资源LiveData
     */
    fun getEssay(type: String): LiveData<Resource<ZhihuItemEntity>>

    /**
     * 获取所有文章
     * @return 包含所有文章列表的资源LiveData
     */
    fun getAllEssays(): LiveData<Resource<EssayDayEntity>>

    /**
     * 根据ID获取特定文章
     * @param id 文章ID
     * @return 包含特定文章的资源LiveData
     */
    fun getEssayById(id: Int): LiveData<Resource<EssayDayEntity>>

    /**
     * 保存文章到本地数据库
     * @param essay 文章实体
     */
    suspend fun saveEssay(essay: EssayDayEntity)

    /**
     * 刷新文章数据（从网络获取最新数据）
     */
    suspend fun refreshEssay()
}