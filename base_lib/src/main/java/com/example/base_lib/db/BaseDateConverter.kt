package com.example.base_lib.db

import androidx.room.TypeConverter
import java.util.Date

/**
 * 基础的日期转换器
 */
open class BaseDateConverter {

    @TypeConverter
    open fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    open fun dataToTimestamp(date: Date?): Long? = date?.time
}