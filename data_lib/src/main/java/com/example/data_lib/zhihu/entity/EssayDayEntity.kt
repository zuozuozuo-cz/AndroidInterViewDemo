package com.example.data_lib.zhihu.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data_lib.zhihu.model.Essay

@Entity(tableName = "essays")
data class EssayDayEntity(

    /**
     * 文章唯一标识ID，主键，自动生成
     */
    @PrimaryKey(autoGenerate = true)
    override val id: Int = 0,

    /**
     * 文章作者
     */
    override val author: String = "",

    /**
     * 文章标题
     */
    override val title: String = "",
    /**
     * 文章摘要
     */
    override val digest: String = "",
    /**
     * 文章完整内容
     */
    override val content: String = "",
    /**
     * 字数统计
     */
    override val wc: Long = 0,

    val dataCurr: String = ""
) : Essay