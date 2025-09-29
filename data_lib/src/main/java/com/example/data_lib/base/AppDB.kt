package com.example.data_lib.base

import android.content.Context
import androidx.room.Database
import androidx.room.TypeConverters
import com.example.base_lib.db.BaseDatabase
import com.example.base_lib.executors.AppExecutors
import com.example.data_lib.member.dao.SnhDao
import com.example.data_lib.member.entity.SnhMemberEntity
import com.example.data_lib.zhihu.converter.DateConverter
import com.example.data_lib.zhihu.dao.EssayDao
import com.example.data_lib.zhihu.dao.ZhihuDao
import com.example.data_lib.zhihu.entity.EssayDayEntity
import com.example.data_lib.zhihu.entity.ZhihuItemEntity

@Database(
    entities = [EssayDayEntity::class, ZhihuItemEntity::class, SnhMemberEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDB : BaseDatabase() {
    //提供文章DAO访问接口
    abstract fun essayDao(): EssayDao

    abstract fun zhihuDao(): ZhihuDao

    abstract fun snhDao(): SnhDao

    companion object {
        fun getInstance(
            context: Context,
            executors: AppExecutors
        ): AppDB {
            return getInstance(
                context,
                AppDB::class.java,
                executors,
            ) as AppDB
        }
    }

}