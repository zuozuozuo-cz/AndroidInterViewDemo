package com.example.base_lib.db

import androidx.sqlite.db.SupportSQLiteDatabase

// 提供默认实现的抽象类
abstract class AbsDbCallback : IDbCallback {
    override fun onCreate(db: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun onOpen(db: SupportSQLiteDatabase) {
        TODO("Not yet implemented")
    }
}