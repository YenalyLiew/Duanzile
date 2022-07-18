package com.yenaly.duanzile.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yenaly.duanzile.databinding.LayoutRvWithRefreshBinding
import com.yenaly.duanzile.ui.adapter.DuanziRvAdapter
import com.yenaly.duanzile.ui.viewmodel.main.HomeViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 15:37
 */
class AllTextFragment : YenalyFragment<LayoutRvWithRefreshBinding, HomeViewModel>() {

    private val adapter by unsafeLazy { DuanziRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.adapter = adapter
        collectAllText()
        binding.srlHome.setOnRefreshListener {
            collectAllText()
        }
    }

    override fun liveDataObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }
    }

    private fun collectAllText() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getAllText().collectLatest(adapter::submitData)
        }
    }
}