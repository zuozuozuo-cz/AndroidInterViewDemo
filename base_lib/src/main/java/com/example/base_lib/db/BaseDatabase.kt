package com.example.base_lib.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.base_lib.executors.AppExecutors

/**
 * 基础数据库接口
 * 定义数据库的基本配置和方法
 */
@TypeConverters()
abstract class BaseDatabase : RoomDatabase() {

    // 数据库创建状态
    private val _isDatabaseCreated = MutableLiveData<Boolean>()
    val databaseCreated: LiveData<Boolean> = _isDatabaseCreated

    companion object {
        @Volatile
        private var sInstance: BaseDatabase? = null

        const val DATABASE_NAME = "dazuozuo.db"

        /**
         * 获取数据库实例（通用方法）
         */
        fun <T : BaseDatabase> getInstance(
            context: Context,
            databaseClass: Class<T>,
            executors: AppExecutors,
        ): T {
            (sInstance as? T)?.let { return it }
            return synchronized(this) {
                (sInstance as? T) ?: buildDatabase(
                    context,
                    databaseClass,
                    executors,
                ).also {
                    sInstance = it
                }
            }
        }

        /**
         * 构建数据库通用方法
         */
        private fun <T : BaseDatabase> buildDatabase(
            context: Context,
            klass: Class<T>,
            executors: AppExecutors,
            vararg typeConverters: Any
        ): T {
            val builder = Room.databaseBuilder(
                context, klass, DATABASE_NAME
            )

            // 添加转换器
            typeConverters.forEach { converter ->
                builder.addTypeConverter(converter)
            }

            return builder.addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    executors.diskIO.execute {
                        DbCallbackHelper.dispatchOnCreate(db)
                        sInstance?.setDatabaseCreated()
                    }
                }
            }).addMigrations(*DbCallbackHelper.getUpdateConfig())
                .build()
        }
    }

    protected fun setDatabaseCreated() {
        _isDatabaseCreated.postValue(true)
    }
}