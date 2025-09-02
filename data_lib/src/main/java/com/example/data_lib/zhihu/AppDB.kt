package com.example.data_lib.zhihu

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data_lib.zhihu.architecture.DbCallbackHelper
import com.example.base_lib.executors.AppExecutors
import com.example.data_lib.zhihu.converter.DateConverter
import com.example.data_lib.zhihu.dao.EssayDao
import com.example.data_lib.zhihu.dao.ZhihuDao
import com.example.data_lib.zhihu.entity.essay.EssayDayEntity
import com.example.data_lib.zhihu.entity.essay.ZhihuItemEntity

@Database(
    entities = [EssayDayEntity::class, ZhihuItemEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class AppDB : RoomDatabase() {
    //提供文章DAO访问接口
    abstract fun essayDao(): EssayDao

    abstract fun zhihuDao(): ZhihuDao

    //数据库创建状态
    private val _isDatabaseCreate = MutableLiveData<Boolean>()
    val databaseCreated: LiveData<Boolean> = _isDatabaseCreate

    companion object {
        @Volatile
        private var sInstance: AppDB? = null// 单例

        @VisibleForTesting
        const val DATABSE_NAME: String = "canking.db"

        fun getInstance(context: Context, executors: AppExecutors): AppDB {
            return sInstance ?: synchronized(this){
                sInstance?:buildDatabase(context.applicationContext,executors).also{
                    sInstance = it
                }
            }
        }

        private fun buildDatabase(applicationContext: Context, executors: AppExecutors): AppDB {
            return Room.databaseBuilder(applicationContext,AppDB::class.java, DATABSE_NAME)
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        executors.diskIO.execute{
                            addDelay()

                            val database = getInstance(applicationContext, executors)

                            DbCallbackHelper.dispatchOnCreate(db)

                            database.setDatabaseCreated()
                        }
                    }
                })
                .addMigrations(*DbCallbackHelper.getUpdateConfig())
                .build()
        }

        private fun addDelay() {
            try {
                Thread.sleep(4000)
            }catch (ignored:InterruptedException){

            }
        }
    }

    private fun updateDatebaseCreated(context: Context){
        if (context.getDatabasePath(DATABSE_NAME).exists()){
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated(){
        _isDatabaseCreate.postValue(true)
    }


}