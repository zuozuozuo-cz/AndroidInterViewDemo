package com.example.androidinterviewdemo

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.text).setOnClickListener{
            val path = "/mvvm_study/main"
            val result = ARouter.getInstance().build(path).navigation()
            if (result == null) {
                Log.e("ARouter", "$path 路由未注册！")
            } else {
                Log.d("ARouter", "$path 路由已注册！")
            }

            ARouter.getInstance()
                .build("/mvvm_study/main")
                .navigation()
        }
    }
}