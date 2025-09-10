package com.example.mvvm_study.ui

import android.os.Bundle
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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.base_lib.constant.Constant
import com.example.base_lib.executors.AppExecutors
import com.example.base_lib.net.NetEngine
import com.example.base_lib.viewmodel.BaseViewModelFactory
import com.example.data_lib.base.DatabaseProvider
import com.example.data_lib.zhihu.entity.ZhihuItemEntity
import com.example.data_lib.zhihu.repository.EssayRepository
import com.example.mvvm_study.R
import com.example.mvvm_study.ui.adapter.EssayListAdapter
import com.example.mvvm_study.viewmodel.EssayState
import com.example.mvvm_study.viewmodel.EssayViewModel
import kotlinx.coroutines.launch

class EssayFragment : Fragment() {

    companion object {
        const val TAG = Constant.COMMON_TAG + "EssayFragment"
        const val TYPE_BASE = 1
    }

    private lateinit var mListView: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var mAdapter: EssayListAdapter

    //    private val viewModel: EssayViewModel by viewModels()
//    private val repository: EssayRepository by lazy {
//        EssayRepository(
//            AppDB.getInstance(requireActivity(), AppExecutors()).zhihuDao(),
//            AppExecutors(),
//            netEngine = TODO()
//        )
//    }

    private val factory = BaseViewModelFactory(
        mapOf(
            EssayViewModel::class.java to {
                EssayViewModel(
                    requireActivity().application,
                    EssayRepository(
                        DatabaseProvider.getZhihuDao(),
                        AppExecutors(),
                        NetEngine.getInstance()
                    )
                )
            }
        )
    )

    private val viewModel: EssayViewModel by lazy {
        ViewModelProvider(this, factory).get(EssayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.essay_fragment, container, false)
        mListView = view.findViewById(R.id.products_list)
        refreshLayout = view.findViewById(R.id.refresh)
        initView()
        subscribeUi()
        return view
    }

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is EssayState.Loading -> {
                            // 显示加载
                            refreshLayout.isRefreshing = true
                        }


                        is EssayState.Success -> {
                            // 显示数据 state.essays
                            refreshLayout.isRefreshing = false
                            state.essay?.let {
                                updateUI(it)
                            }
                            Toast.makeText(requireActivity(), "succeed", Toast.LENGTH_LONG).show()
                        }

                        is EssayState.Error -> {
                            // 显示错误 state.message
                            refreshLayout.isRefreshing = false
                            Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            refreshLayout.isRefreshing = false
                            Toast.makeText(requireActivity(), "else", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
//        viewModel.getEssayData().observe(viewLifecycleOwner, Observer { resources ->
//            resources?.data?.let { data ->
//                when (resources.status) {
//                    Status.SUCCEED -> {
//                        updateUI(data)
//                        Toast.makeText(requireActivity(), "succeed", Toast.LENGTH_LONG).show()
//                    }
//
//                    Status.LOADING -> {
//                        Toast.makeText(
//                            requireActivity(),
//                            "DB loaded ${resources.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    Status.ERROR -> {
//                        Toast.makeText(requireActivity(), "error", Toast.LENGTH_SHORT).show()
//                    }
//
//                    else -> {
//                        Toast.makeText(requireActivity(), "else", Toast.LENGTH_SHORT).show()
//
//                    }
//                }
//            }
//            refreshLayout.isRefreshing = false
//        })
    }

    private fun updateUI(data: ZhihuItemEntity) {
        val list = mutableListOf<EssayListAdapter.MultiItem>()
        data.stories?.forEach {
            list.add(EssayListAdapter.MultiItem(it, TYPE_BASE))
        }

        data.top_stories?.forEach {
            list.add(EssayListAdapter.MultiItem(it, TYPE_BASE))
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
        val footView = View(requireActivity()).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelOffset(R.dimen.bottom_btn_bg_heigh)
            )
        }
        // 初始化适配器
        mAdapter = EssayListAdapter(requireActivity(), mutableListOf())
//        mAdapter.onAttachedToRecyclerView(mListView)
        mAdapter.addFooterView(footView)
        mAdapter.animationEnable = true
        mAdapter.setOnItemClickListener { _, _, _ ->

        }
        mListView.adapter = mAdapter
        refreshLayout.setProgressViewOffset(
            false, 0, TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 28f, resources.displayMetrics
            ).toInt()
        )
        refreshLayout.setOnRefreshListener {
            mListView.postDelayed({
                viewModel.refreshEssay()
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