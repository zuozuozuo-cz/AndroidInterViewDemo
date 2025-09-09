package com.example.mvvm_study.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.viewmodel.BaseViewModelFactory
import com.example.data_lib.zhihu.DatabaseProvider
import com.example.data_lib.zhihu.repository.SnhMemberRepository
import com.example.mvvm_study.R
import com.example.mvvm_study.viewmodel.SnhMemberViewModel
import kotlinx.coroutines.launch

class SNH48Fragment : Fragment() {
    companion object {
        const val TAG = Constant.COMMON_TAG + "SNH48Fragment"
        const val TYPE_BASE = 1
    }

    private lateinit var mListView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private val factory = BaseViewModelFactory(
        mapOf(
            SnhMemberViewModel::class.java to {
                SnhMemberViewModel(
                    requireActivity().application, SnhMemberRepository(
                        DatabaseProvider.getShnDao(), AppExecutors(), NetEngine.getInstance()
                    )
                )
            }
        )
    )

    private val viewModel: SnhMemberViewModel by lazy {
        ViewModelProvider(this, factory).get(SnhMemberViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.snh48_fragment, container, false)
        mListView = view.findViewById(R.id.recyclerView)
        refreshLayout = view.findViewById(R.id.refresh)
        initView()
        subcribeUI()
        return view
    }

    private fun subcribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }

    private fun initView() {
        Log.e(TAG, "========== initView ==========")

    }
}