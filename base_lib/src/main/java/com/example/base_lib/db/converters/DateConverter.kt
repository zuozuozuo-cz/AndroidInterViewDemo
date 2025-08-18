package com.example.base_lib.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.sql.Date

/**
 * Room 类型转换器
 * 用于将数据库不支持的类型，例如Date，List<ZhihuStoriesEntity>
 * 转换成可储存的基本类型
 */
class DateConverter {
    private val gson: Gson = GsonBuilder().create()

    /**
     * long -> date
     * 数据库存的是long,时间戳，取出时转为Date
     */

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let {
            Date(it)
        }
    }

    /**
     * Date -> long
     * 数据库存的是long 时间戳，取出时转为Date
     */
    @TypeConverter
    fun toTimesTamp(date: Date?): Long? {
        return date?.time
    }

    /**
     * String(json) -> List<ZhihuStoriesEntity>
     * 数据库存的是JSON字符串，取出时转成对象列表
     */
    @TypeConverter
    fun toZhihuStoriesList(json: String?): List<ZhihuStoriesEntity>?{
        if (json.isNullOrBlank()) return  emptyList()
    }

}