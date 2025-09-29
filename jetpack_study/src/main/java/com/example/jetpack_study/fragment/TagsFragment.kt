package com.example.jetpack_study.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.jetpack_study.R
import com.example.jetpack_study.databinding.FragmentCategoryBinding
import com.example.jetpack_study.databinding.FragmentTagBinding

open class TagsFragment : BaseFragment() {
    protected val tagName: String = this::class.java.simpleName

    lateinit var binding: FragmentTagBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tagName, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnJumpToUser.setOnClickListener {
            val navController = findNavController()
            // navController.navigate(R.id.user_fragment)
            navController.navigate(
                R.id.user_fragment,
                null,
                NavOptions.Builder().setPopUpTo(
                    R.id.home_fragment, // 清空 home_fragment -> user_fragment 之间的 Fragment栈，让站内只剩下user_fragment
                    inclusive = false, // 清空同时，是否保留home_fragment
                    saveState = true // 是否保留 状态
                ).build()
            )
        }
    }
}