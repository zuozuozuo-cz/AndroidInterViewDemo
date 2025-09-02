package com.example.data_lib.zhihu.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data_lib.zhihu.entity.essay.ZhihuItemEntity

@Dao
interface ZhihuDao {
    /**
     * 加载最新的知乎文章
     * 按ID降序排序后获取第一条记录
     *
     * 注意：原SQL中的 `order by id desc, id limit 0,1` 可简化为 `ORDER BY id DESC LIMIT 1`
     *
     * @return LiveData 包装的最新知乎文章实体，可观察数据变化
     */
    @Query("SELECT * FROM zhihulist ORDER BY id DESC LIMIT 0,1")
    fun loadLatestZhihuItem(): LiveData<ZhihuItemEntity?>

    /**
     * 插入或更新知乎文章项
     * @param item 要保存的知乎文章实体
     * 冲突策略：如果已存在相同主键的记录，则替换
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveZhihuItem(itemEntity: ZhihuItemEntity)
}