package com.example.data_lib.zhihu

import android.content.Context
import com.example.base_lib.executors.AppExecutors
import com.example.data_lib.zhihu.dao.EssayDao
import com.example.data_lib.zhihu.dao.SnhDao
import com.example.data_lib.zhihu.dao.ZhihuDao

object DatabaseProvider {
    @Volatile
    private var database: AppDB? = null

    /**
     * 初始化数据库
     * 必须在应用启动时调用
     * @param context 应用上下文
     * @param appExecutors 线程执行器
     * @param typeConverters 类型转换器
     */
    fun initialize(context: Context, appExecutors: AppExecutors) {
        database = AppDB.getInstance(context, appExecutors)
    }

    /**
     * 获取数据库实例
     */
    fun getDatabase(): AppDB {
        return database
            ?: throw IllegalStateException(
                "Database not initialized. " +
                        "call DatabaseProvider.initialize() first"
            )
    }

    /**
     * 检查数据库是否初始化
     */
    fun isInitialized(): Boolean = database != null

    /**
     * 获取EssayDao
     */
    fun getEssayDao(): EssayDao = getDatabase().essayDao()

    /**
     * 获取ZhihuDao
     */
    fun getZhihuDao(): ZhihuDao = getDatabase().zhihuDao()

    /**
     * 获取SnhDao
     */
    fun getShnDao(): SnhDao = getDatabase().snhDao()
}