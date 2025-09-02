package com.example.mvvm_study.ui

import ImageUtils
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base_lib.constant.Constant
import com.example.mvvm_study.R

class SNH48Fragment : Fragment() {
    companion object {
        const val TAG = Constant.COMMON_TAG + "SNH48Fragment"
        const val TYPE_BASE = 1
    }

    private lateinit var mListView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.snh48_fragment, container, false)
        mListView = view.findViewById(R.id.recyclerView)
        refreshLayout = view.findViewById(R.id.refresh)
//        view.findViewById<Button>(R.id.btn_share).setOnClickListener {
//            val path = "/storage/emulated/0/Pictures/1756802848754.jpg"
//            ImageUtils.openImage(requireActivity(),path)
//        }
        initView()
        subcribeUI()
        return view
    }

    private fun subcribeUI() {
        Log.e(TAG,"========== subcribeUI ==========")
    }

    private fun initView() {
        Log.e(TAG,"========== initView ==========")

    }
}