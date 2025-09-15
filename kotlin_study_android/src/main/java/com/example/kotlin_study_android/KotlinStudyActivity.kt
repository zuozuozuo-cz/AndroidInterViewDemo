package com.example.kotlin_study_android

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KotlinStudyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_kotlin_study)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // KTX
        // 与当前组件的生命周期绑定，当Activity/Fragment 销毁的时候
        // 就取消了
        // 内置主线程
        // immediate 会判断当前线程是不是主线程
        lifecycleScope.launch {
            netcall()
        }



        CoroutineScope(Dispatchers.Main).launch {
            // 挂起函数执行完成后，就切回到主线程了
            val netcall = netcall()
            Log.e("aaa", netcall)
        }

        CoroutineScope(Dispatchers.Main).launch {
            Log.e("aaa", "1")
            launch(Dispatchers.IO) {

                Log.e("aaa", "2")
            }
            // launch 执行完成后立即执行
            Log.e("aaa", "3")
            // 打印顺序 132
        }

        CoroutineScope(Dispatchers.Main).launch {
            Log.e("aaa", "1")

            withContext(Dispatchers.IO) {

                Log.e("aaa", "2")
            }
            // 在主线程中执行，但是会等withContext 内部代码执行完成后再执行
            Log.e("aaa", "3")
            // 打印顺序123
        }

    }

    /**
     * 执行这个函数的时候，它所在的协程就被挂起了
     *  把写成核线程暂时分离，所以挂起函数只有在协程中调用才有意义
     */
    suspend fun netcall(): String {
        return "aaa"
    }
}

class MyViewModel : ViewModel() {
    fun aa() {
        viewModelScope.launch {

        }
    }
}
