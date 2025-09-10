package com.example.data_lib.zhihu.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data_lib.zhihu.entity.EssayDayEntity

@Dao
interface EssayDao {

    /**
     * 根据日期加载文章
     * @param day 查询日期（格式如 "2023-06-15"）
     * @return LiveData 包装的文章实体，可观察数据变化
     */
    @Query("SELECT * FROM essays where dataCurr = :day")
    fun loadEssayDao(day: String): LiveData<EssayDayEntity?>


    /**
     * 加载最新一篇文章
     * 按文章ID降序排序后获取第一条记录
     * @return LiveData 包装的最新文章实体
     */
    @Query("SELECT * FROM essays ORDER BY id DESC LIMIT 1")
    fun loadLastEssayDao(): LiveData<EssayDayEntity?>

    /**
     * 保存或更新文章
     * @param entity 要保存的文章实体
     * 冲突策略：如果已存在相同主键的记录，则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEssayItem(entity: EssayDayEntity)
}