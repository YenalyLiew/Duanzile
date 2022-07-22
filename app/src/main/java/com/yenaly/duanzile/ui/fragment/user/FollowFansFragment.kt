package com.yenaly.duanzile.ui.fragment.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.yenaly.duanzile.databinding.LayoutRvWithRefreshBinding
import com.yenaly.duanzile.ui.activity.FOLLOW_FANS_ARGUMENT
import com.yenaly.duanzile.ui.adapter.SubscribeFanRvAdapter
import com.yenaly.duanzile.ui.viewmodel.FollowFansViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.arguments
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest


const val FOLLOW = "follow"
const val FANS = "fans"

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 21:53
 */
class FollowFansFragment : YenalyFragment<LayoutRvWithRefreshBinding, FollowFansViewModel>() {

    private val adapter by unsafeLazy { SubscribeFanRvAdapter() }
    private val type by arguments(FOLLOW_FANS_ARGUMENT, FOLLOW)

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.adapter = adapter
        binding.rvHome.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.srlHome.setOnRefreshListener {
            getUserList()
        }
    }

    override fun liveDataObserve() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }

        getUserList()
    }

    private fun getUserList() {
        when (type) {
            FOLLOW -> followImpl()
            FANS -> fansImpl()
            else -> throw IllegalStateException("???")
        }
    }

    private fun followImpl() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserSubscribeList(viewModel.userID).collectLatest(adapter::submitData)
        }
    }

    private fun fansImpl() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getUserFanList(viewModel.userID).collectLatest(adapter::submitData)
        }
    }
}