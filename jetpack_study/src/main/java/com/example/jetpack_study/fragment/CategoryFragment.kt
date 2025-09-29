package com.example.jetpack_study.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jetpack_study.R
import com.example.jetpack_study.databinding.FragmentCategoryBinding

open class CategoryFragment : BaseFragment() {
    protected val tagName: String = this::class.java.simpleName

    lateinit var binding: FragmentCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tagName, "onCreate" + savedInstanceState?.getString("category"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnJumpToTag.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.tags_fragment)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("category", "我是Category")
    }


}