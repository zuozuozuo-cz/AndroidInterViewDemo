package com.example.mvvm_study.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.base_lib.net.Status
import com.example.data_lib.zhihu.entity.ZhihuItemEntity
import com.example.data_lib.zhihu.repository.EssayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EssayViewModel(application: Application, private val essayRepository: EssayRepository) :
    AndroidViewModel(application) {
    // ui 状态
    private val _uiState = MutableStateFlow<EssayState>(EssayState.Loading)
    val uiState: StateFlow<EssayState> = _uiState.asStateFlow()

    init {
        loadEssays()
    }


    /**
     * 加载文章数据
     */
    fun loadEssays() {
        viewModelScope.launch {
            _uiState.value = EssayState.Loading

            essayRepository.loadEssays().asFlow().collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        _uiState.value = EssayState.Loading
                    }

                    Status.SUCCEED -> {
                        _uiState.value = EssayState.Success(resource.data ?: null)
                    }

                    Status.ERROR -> {
                        _uiState.value = EssayState.Error(resource.message ?: "未知错误")
                    }

                    else -> {
                        _uiState.value = EssayState.Error("请求错误")
                    }
                }
            }

        }
    }

    /**
     * 刷新逻辑
     */
    fun refreshEssay() {
        loadEssays()
    }
}

//class EssayViewModel(application: Application, private val repository: EssayRepository) :
//    AndroidViewModel(application) {
//
//    private val cache: MediatorLiveData<Resource<ZhihuItemEntity>> = MediatorLiveData()
//
//    fun getEssayData(): LiveData<Resource<ZhihuItemEntity>> {
//        if (cache.value == null) {
//            cache.addSource(repository.laodEssayData()) {
//                cache.value = it
//            }
//        }
//        return cache
//    }
//
//    fun updateCache() {
//        cache.addSource(repository.update()) {
//            cache.value = it
//        }
//    }
//}
/**
 * 为什么要封装密封类？？？
 */
sealed class EssayState {
    object Loading : EssayState()
    object Refreshing : EssayState()
    data class Success(val essay: ZhihuItemEntity?) : EssayState()
    data class Error(val message: String) : EssayState()
}