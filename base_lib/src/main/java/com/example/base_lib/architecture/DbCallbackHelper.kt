package com.example.base_lib.architecture

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.Collections

/**
 * 数据库回调辅助类
 * 用于集中管理数据库的创建和升级逻辑
 */
object DbCallbackHelper {

    // 保存所有数据库回调对象
    // 线程安全
    private val mDbCallbacks = Collections.synchronizedList(mutableListOf<IDbCallback>())


    fun registerCallbacks(callback: IDbCallback){
        mDbCallbacks.add(callback)
    }

    fun dispatchOnCreate(db: SupportSQLiteDatabase) {
        mDbCallbacks.forEach { absDbCallback ->
            absDbCallback.onCreate(db)
        }
    }

    fun dispatchUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        mDbCallbacks.forEach { absDbCallback ->
            absDbCallback.onUpgrade(db, oldVersion, newVersion)

        }
    }

    /**
     * 获取数据库迁移配置
     * @return 迁移配置数组
     */
    fun getUpdateConfig():Array<Migration>{
        return arrayOf(
            // 版本1→2的迁移
            object :Migration(1,2){
                override fun migrate(db: SupportSQLiteDatabase) {
                    dispatchUpgrade(db,1,2)
                }
            },
            // 版本2→3的迁移
            object :Migration(2,3){
                override fun migrate(db: SupportSQLiteDatabase) {
                    dispatchUpgrade(db,2,3)
                }

            }
            // 以此类推
        )
    }
}