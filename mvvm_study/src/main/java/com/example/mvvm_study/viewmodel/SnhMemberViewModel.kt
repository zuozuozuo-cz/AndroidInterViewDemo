package com.example.mvvm_study.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.base_lib.net.Status
import com.example.data_lib.zhihu.entity.member.SnhMemberEntity
import com.example.data_lib.zhihu.entity.member.Team
import com.example.data_lib.zhihu.repository.SnhMemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SnhMemberViewModel(
    application: Application,
    private val memberRepository: SnhMemberRepository
) : AndroidViewModel(application) {
    // ui 状态
    private val _uiState = MutableStateFlow<MemberState>(MemberState.Loading)
    val uiState: StateFlow<MemberState> = _uiState.asStateFlow()

    init {
        loadMembers(Team.SNH48)
    }

    /**
     * 加载数据
     */
    fun loadMembers(team: Team) {
        viewModelScope.launch {
            memberRepository.loadMembers().asFlow().distinctUntilChanged()
                .collect { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                        _uiState.value = MemberState.Loading
                    }

                    Status.SUCCEED -> {
                        _uiState.value = MemberState.Success(resource.data ?: emptyList())
                    }

                    Status.ERROR -> {
                        _uiState.value = MemberState.Error(resource.message ?: "未知错误")
                    }

                    else -> {
                        _uiState.value = MemberState.Error("请求错误")
                    }
                }
            }
        }
    }

    fun refresh(team: Team) {
        loadMembers(team)
    }
}

/**
 * 为什么要封装密封类？？？
 */
sealed class MemberState {
    object Loading : MemberState()
    object Refreshing : MemberState()
    data class Success(val members: List<SnhMemberEntity>) : MemberState()
    data class Error(val message: String) : MemberState()
}