package com.example.jetpack_study

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_lib.routers.ModuleRoutes
import com.example.jetpack_study.databinding.ActivityJetPackMainBinding

@Route(path = ModuleRoutes.MODULE_JETPACK_HOME)
class JetPackMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJetPackMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJetPackMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}