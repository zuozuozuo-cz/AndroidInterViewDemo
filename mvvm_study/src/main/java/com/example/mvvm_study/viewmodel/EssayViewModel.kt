package com.example.mvvm_study.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.base_lib.net.Resource
import com.example.data_lib.zhihu.entity.ZhihuItemEntity
import com.example.data_lib.zhihu.repository.EssayRepository

class EssayViewModel(application: Application, private val repository: EssayRepository) :
    AndroidViewModel(application) {

    private val cache: MediatorLiveData<Resource<ZhihuItemEntity>> = MediatorLiveData()

    fun getEssayData(): LiveData<Resource<ZhihuItemEntity>> {
        if (cache.value == null) {
            cache.addSource(repository.laodEssayData()) {
                cache.value = it
            }
        }
        return cache
    }

    fun updateCache() {
        cache.addSource(repository.update()) {
            cache.value = it
        }
    }
}