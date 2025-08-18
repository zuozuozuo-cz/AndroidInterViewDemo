package com.example.mvvm_study.db.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm_study.db.entity.EssayDayEntity

@Dao
interface EssayDao {
    /**
     * 根据日期查询文章
     */
    @Query("SELECT * FROM essays WHERE dataCurr = :day")
    fun getEssayByDate(day: String): LiveData<EssayDayEntity>

    /**
     * 获取最后一条文章
     */
    @Query("SELECT * FROM essays ORDER BY id DESC LIMIT 1")
    fun getLastEssay(): LiveData<EssayDayEntity>

    /**
     * 插入或者更新文章
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEssayItem(entity: EssayDayEntity)
}