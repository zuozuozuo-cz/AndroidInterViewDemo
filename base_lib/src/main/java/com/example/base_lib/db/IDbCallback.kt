package com.example.base_lib.db

import androidx.sqlite.db.SupportSQLiteDatabase

// 定义数据库升级契约
interface IDbCallback {
    fun onCreate(db: SupportSQLiteDatabase)
    fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int)
    fun onOpen(db: SupportSQLiteDatabase)
}