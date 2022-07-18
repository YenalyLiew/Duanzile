package com.yenaly.duanzile.ui.fragment.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.yenaly.duanzile.TO_TEXT_VIDEO_SPLIT_FRAGMENT
import com.yenaly.duanzile.databinding.LayoutRvWithRefreshBinding
import com.yenaly.duanzile.ui.adapter.SimpleDuanziRvAdapter
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
class TextPhotoItemFragment : YenalyFragment<LayoutRvWithRefreshBinding, UserViewModel>() {

    private val type by arguments(TO_TEXT_VIDEO_SPLIT_FRAGMENT, WORK)

    private val adapter by unsafeLazy { SimpleDuanziRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.rvHome.adapter = adapter
    }

    override fun liveDataObserve() {
        if (type == WORK) {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getUserDuanzi(viewModel.userID).collectLatest(adapter::submitData)
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getUserLikedDuanzi(viewModel.userID).collectLatest(adapter::submitData)
            }
        }
    }
}