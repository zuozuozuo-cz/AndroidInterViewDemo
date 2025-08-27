package com.example.mvvm_study.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base_lib.constant.Constant
import com.example.mvvm_study.R
import com.example.mvvm_study.ui.adapter.EssayListAdapter

class EssayFragment : Fragment(){

    companion object{
        const val TAG = Constant.COMMON_TAG + "EssayFragment"
        const val type_base = 0
    }

    private lateinit var mListView:RecyclerView
    private lateinit var refreshLayout:SwipeRefreshLayout
    private lateinit var mAdapter: EssayListAdapter
//    private lateinit var viewModel:EssayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.essay_fragment,container,false)
        mListView = view.findViewById(R.id.products_list)
        refreshLayout = view.findViewById(R.id.refresh)
        initView()
        subscribeUi()
        return view
    }

    private fun subscribeUi() {

    }

    private fun initView() {

    }
}