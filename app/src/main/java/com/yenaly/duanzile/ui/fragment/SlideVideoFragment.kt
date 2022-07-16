package com.yenaly.duanzile.ui.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.FragmentSlideVideoBinding
import com.yenaly.duanzile.ui.activity.IToggleToolbar
import com.yenaly.duanzile.ui.adapter.SlideVideoRvAdapter
import com.yenaly.duanzile.ui.viewmodel.main.SlideVideoViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.unsafeLazy
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 18:25
 */
class SlideVideoFragment : YenalyFragment<FragmentSlideVideoBinding, SlideVideoViewModel>(),
    IToggleToolbar {

    private val adapter by unsafeLazy { SlideVideoRvAdapter() }

    override fun initData(savedInstanceState: Bundle?) {
        binding.vpSlide.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.vpSlide.adapter = adapter
    }

    override fun liveDataObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.getSlideVideo().collectLatest(adapter::submitData)
        }
    }

    override fun toggleToolbar(toolbar: Toolbar) {
        toolbar.title = "划一划"
        toolbar.setSubtitle(R.string.app_name)
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setSubtitleTextColor(Color.WHITE)
    }
}