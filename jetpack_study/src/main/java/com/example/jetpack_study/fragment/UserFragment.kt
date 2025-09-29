package com.example.jetpack_study.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.jetpack_study.R
import com.example.jetpack_study.databinding.FragmentTagBinding
import com.example.jetpack_study.databinding.FragmentUserBinding
import kotlin.random.Random

open class UserFragment : BaseFragment() {
    protected val tagName: String = this::class.java.simpleName

    lateinit var binding: FragmentUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tagName, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnJumpToBack.setOnClickListener {
//            findNavController().navigate(
//                R.id.category_fragment, null, NavOptions.Builder().setRestoreState(true) // 保存恢复页面数据
//                    .build() // category_fragment 重建的时候，savedInstanceState 会有值
//            )
            findNavController()
                .popBackStack(
                    R.id.home_fragment,
                    inclusive = false,  // home_fragment 是否弹出站
                    saveState = true // 是否保存
                )
        }

        binding.btnJumpToSelf.setOnClickListener {
            findNavController().navigate(
                R.id.user_fragment, null, NavOptions.Builder()
                    .setLaunchSingleTop(true) // 这里会重新创建一个新的user_fragment对象来启动
                    .build()
            )
        }
    }
}