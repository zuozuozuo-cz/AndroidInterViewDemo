package com.example.jetpack_study.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class TagsFragment:Fragment() {
    private val tagName: String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tagName, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(tagName, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tagName, "onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tagName, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tagName, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tagName, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tagName, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(tagName, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tagName, "onDestroy")
    }
}