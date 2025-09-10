package com.example.mvvm_study.ui

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.viewmodel.BaseViewModelFactory
import com.example.data_lib.base.DatabaseProvider
import com.example.data_lib.member.entity.SnhMemberEntity
import com.example.data_lib.member.entity.Team
import com.example.data_lib.member.repository.SnhMemberRepository
import com.example.mvvm_study.R
import com.example.mvvm_study.ui.adapter.SNHListAdapter
import com.example.mvvm_study.viewmodel.MemberState
import com.example.mvvm_study.viewmodel.SnhMemberViewModel
import kotlinx.coroutines.launch

class SNH48Fragment : Fragment() {
    companion object {
        const val TAG = Constant.COMMON_TAG + "SNH48Fragment"
        const val TYPE_BASE = 1
    }

    private lateinit var mListView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var mAdapter: SNHListAdapter
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
                // 监听发来的flow
                viewModel.uiState.collect { state ->
                    Log.e(
                        Constant.COMMON_TAG,
                        "SNH48Fragment ======== subcribeUI ======== state:${state}"
                    )
                    when (state) {
                        is MemberState.Loading -> {
                            refreshLayout.isRefreshing = true
                        }

                        is MemberState.Success -> {
                            refreshLayout.isRefreshing = false
                            state.members?.let {
                                updateUI(it)
                            }
                        }

                        is MemberState.Error -> {
                            refreshLayout.isRefreshing = false
                            Toast.makeText(requireActivity(), "error", Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            refreshLayout.isRefreshing = false
                            Toast.makeText(requireActivity(), "else", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateUI(snhMemberEntities: List<SnhMemberEntity>) {
        Log.e(
            Constant.COMMON_TAG,
            "SNH48Fragment ======== updateUI ======== snhMemberEntities:${snhMemberEntities.size}"
        )
        val list = mutableListOf<SNHListAdapter.MultiItem>()
        snhMemberEntities?.forEach {
            list.add(SNHListAdapter.MultiItem(it, EssayFragment.TYPE_BASE))
        }
//        mAdapter.data = list
        mAdapter.setList(list)

        // 添加生命周期观察者示例（空实现）
        lifecycle.addObserver(object : LifecycleObserver {
            override fun hashCode(): Int {
                return super.hashCode()
            }
        })
    }

    private fun initView() {
        Log.e(TAG, "========== initView ==========")
        val footView = View(requireActivity()).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelOffset(R.dimen.bottom_btn_bg_heigh)
            )
        }
        // 初始化适配器
        mAdapter = SNHListAdapter(requireActivity(), mutableListOf())
//        mAdapter.onAttachedToRecyclerView(mListView)
        mAdapter.addFooterView(footView)
        mAdapter.animationEnable = true
        mAdapter.setOnItemClickListener { _, _, _ ->

        }
        mListView.layoutManager = (LinearLayoutManager(requireActivity()))
        mListView.adapter = mAdapter
        refreshLayout.setProgressViewOffset(
            false, 0, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics
            ).toInt()
        )
        refreshLayout.setOnRefreshListener {
            mListView.postDelayed({
                viewModel.refresh(Team.SNH48)
            }, 2000)
        }

        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white)
        refreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_light,
            android.R.color.holo_red_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_green_light
        )
        refreshLayout.isRefreshing = true
    }
}