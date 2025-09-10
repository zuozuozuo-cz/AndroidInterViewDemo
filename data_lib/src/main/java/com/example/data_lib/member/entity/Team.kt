package com.example.data_lib.member.entity

/**
 * 团队枚举
 * @param gid 团体 ID
 * @param groupName 中文名称
 */
enum class Team(val gid: Int, val groupName: String) {
    SNH48(10, "SNH48 上海正团"),
    BEJ48(20, "BEJ48 北京姐妹团"),
    GNZ48(30, "GNZ48 广州姐妹团"),
    CKG48(40, "CKG48 重庆姐妹团"),
    CGT48(50, "CGT48 成都姐妹团"),
    JNR48(60, "JNR48 少儿练习生团计划")
}