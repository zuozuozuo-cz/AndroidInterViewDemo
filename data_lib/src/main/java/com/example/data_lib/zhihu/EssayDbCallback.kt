package com.example.data_lib.zhihu

import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data_lib.zhihu.architecture.AbsDbCallback

class EssayDbCallback : AbsDbCallback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        // 文章表创建后初始化逻辑
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 文章表升级后，初始化逻辑
    }

    // 不需要重写 onOpen 除非有特殊逻辑
}