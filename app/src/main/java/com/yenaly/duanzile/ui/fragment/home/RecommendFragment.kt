package com.yenaly.duanzile.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yenaly.duanzile.databinding.FragmentHomeChildBinding
import com.yenaly.duanzile.ui.adapter.HomeRvAdapter
import com.yenaly.duanzile.ui.viewmodel.main.HomeViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 15:36
 */
class RecommendFragment : YenalyFragment<FragmentHomeChildBinding, HomeViewModel>() {

    private val adapter by unsafeLazy { HomeRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.adapter = adapter
        collectRecommend()
        binding.srlHome.setOnRefreshListener {
            collectRecommend()
        }
    }

    override fun liveDataObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }
    }

    private fun collectRecommend() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getRecommend().collectLatest(adapter::submitData)
        }
    }
}