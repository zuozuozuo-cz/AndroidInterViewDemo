package com.example.data_lib.zhihu.base

object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null
}