package com.yenaly.duanzile.ui.fragment.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.yenaly.duanzile.TO_TEXT_VIDEO_SPLIT_FRAGMENT
import com.yenaly.duanzile.databinding.LayoutRvWithRefreshBinding
import com.yenaly.duanzile.ui.adapter.SimpleDuanziVideoRvAdapter
import com.yenaly.duanzile.ui.viewmodel.UserViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.arguments
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/17 017 14:16
 */
class VideoItemFragment : YenalyFragment<LayoutRvWithRefreshBinding, UserViewModel>() {

    private val type by arguments(TO_TEXT_VIDEO_SPLIT_FRAGMENT, WORK)

    private val adapter by unsafeLazy { SimpleDuanziVideoRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.layoutManager = GridLayoutManager(context, 3)
        binding.rvHome.adapter = adapter
        binding.srlHome.setOnRefreshListener {
            getVideo()
        }
    }

    override fun liveDataObserve() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }

        getVideo()
    }

    private fun getVideo() {
        if (type == WORK) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getUserDuanziVideo(viewModel.userID)
                    .collectLatest(adapter::submitData)
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getUserLikedDuanziVideo(viewModel.userID)
                    .collectLatest(adapter::submitData)
            }
        }
    }
}