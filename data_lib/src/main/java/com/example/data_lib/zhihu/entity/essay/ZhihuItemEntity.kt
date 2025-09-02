package com.example.data_lib.zhihu.entity.essay

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "zhihulist")
data class ZhihuItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val date: String? = null,

    val stories: List<ZhihuStoriesEntity>? = null,

    val top_stories: List<ZhihuStoriesEntity>? = null
)
