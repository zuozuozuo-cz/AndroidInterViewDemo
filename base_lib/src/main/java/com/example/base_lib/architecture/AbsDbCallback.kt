package com.example.base_lib.architecture

import androidx.sqlite.db.SupportSQLiteDatabase

abstract class AbsDbCallback {
    abstract fun create(db: SupportSQLiteDatabase)

    abstract fun open()

    abstract fun upgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int)
}