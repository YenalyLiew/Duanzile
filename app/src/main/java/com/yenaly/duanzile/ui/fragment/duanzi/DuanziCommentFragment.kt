package com.yenaly.duanzile.ui.fragment.duanzi

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.yenaly.duanzile.databinding.LayoutRvWithRefreshBinding
import com.yenaly.duanzile.ui.adapter.CommentRvAdapter
import com.yenaly.duanzile.ui.fragment.user.WORK
import com.yenaly.duanzile.ui.viewmodel.DuanziViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 14:10
 */
class DuanziCommentFragment : YenalyFragment<LayoutRvWithRefreshBinding, DuanziViewModel>() {

    private val adapter by unsafeLazy { CommentRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.adapter = adapter
        binding.rvHome.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        getComment()
        binding.srlHome.setOnRefreshListener {
            getComment()
        }
    }

    override fun liveDataObserve() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }

        getComment()
    }

    private fun getComment() {

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getComment(viewModel.duanziID).collectLatest(adapter::submitData)
        }

    }
}