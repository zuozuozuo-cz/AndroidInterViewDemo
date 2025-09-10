package com.example.data_lib.member.entity

/**
 * SNH 接口封装类
 */
data class MemberResponse(
    val total: String,
    val rows: List<SnhMemberEntity>
)
