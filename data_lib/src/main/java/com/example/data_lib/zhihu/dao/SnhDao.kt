package com.example.data_lib.zhihu.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data_lib.zhihu.entity.member.SnhMemberEntity

@Dao
interface SnhDao {

    // 插入数据，若主键冲突则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memberEntity: SnhMemberEntity): Long

    // 批量插入，主键冲突替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(memberEntitys: List<SnhMemberEntity>): List<Long>

    @Update // 更新数据，以主键来匹配
    fun update(memberEntity: SnhMemberEntity)

    @Delete
    fun delete(memberEntity: SnhMemberEntity)

    @Query("SELECT * FROM member WHERE sid = :sid LIMIT 1")
    fun getBySid(sid: String): LiveData<SnhMemberEntity?>

    @Query("SELECT * FROM member ORDER BY sid ASC")
    fun getAll(): LiveData<List<SnhMemberEntity>>

    @Query("DELETE FROM member")
    fun deleteAll()
}