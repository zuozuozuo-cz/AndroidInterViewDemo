package com.example.data_lib.zhihu

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.base_lib.executors.AppExecutors
import com.example.data_lib.zhihu.converter.DateConverter
import com.example.data_lib.zhihu.entity.EssayDayEntity
import com.example.data_lib.zhihu.entity.ZhihuItemEntity

@Database(
    entities = [EssayDayEntity::class, ZhihuItemEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
class AppDB : RoomDatabase() {

    companion object{
        @Volatile
        private val sInstance: AppDB? = null

        @VisibleForTesting
        const val DATABSE_NAME: String = "canking.db"

        fun getInstance(context: Context,executors: AppExecutors): AppDB{
            return Room.databaseBuilder(context, AppDB::class.java,DATABSE_NAME)
                .addCallback(object : Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors.diskIO.execute {
                            // 模拟耗时操作
                            addDelay()

                            val instance = getInstance(context, executors)


                        }
                    }
                })
                .addMigrations()
                .build()
        }
    }

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }
}