package com.example.androidinterviewdemo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.androidinterviewdemo.adapter.HomeListAdapter
import com.example.androidinterviewdemo.entity.HomeItemEntity
import com.example.base_lib.routers.ModuleRoutes

class MainActivity : AppCompatActivity() {
    var homeItemList = arrayListOf<HomeItemEntity>(
        HomeItemEntity("MVVM 学习", ModuleRoutes.MODULE_MVVM_HOME),
        HomeItemEntity("JetPack 学习", ""),
        HomeItemEntity("Kotlin 学习", ""),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val homeListAdapter = HomeListAdapter(homeItemList, this)
        recyclerView.adapter = homeListAdapter
    }
}

