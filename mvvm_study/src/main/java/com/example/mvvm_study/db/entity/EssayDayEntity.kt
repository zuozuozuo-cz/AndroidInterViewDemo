package com.example.mvvm_study.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mvvm_study.model.Essay
import java.security.MessageDigest

@Entity(tableName = "essays")
data class EssayDayEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0, // 实现接口的 id，同时作为主键

    var dataCur: String = "",  // 额外字段：日期（接口里没有，但表里需要）

    override val author: String = "",// 作者（实现接口）

    override val title: String = "",// 标题（实现接口）

    override val digest: String = "",// 摘要（实现接口）

    override val content: String = "", // 正文（实现接口）

    override val wc: Long = 0L // 字数（实现接口）

) : Essay