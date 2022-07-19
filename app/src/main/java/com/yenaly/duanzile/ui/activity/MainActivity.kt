package com.yenaly.duanzile.ui.activity

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import cn.jzvd.Jzvd
import com.google.android.material.button.MaterialButton
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.ActivityMainBinding
import com.yenaly.duanzile.ui.fragment.HomeFragment
import com.yenaly.duanzile.ui.fragment.MessageFragment
import com.yenaly.duanzile.ui.fragment.PersonalFragment
import com.yenaly.duanzile.ui.fragment.SlideVideoFragment
import com.yenaly.duanzile.ui.viewmodel.MainViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.SystemStatusUtil
import com.yenaly.yenaly_libs.utils.isAppDarkMode
import com.yenaly.yenaly_libs.utils.setSystemBarIconLightMode
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.view.BottomNavigationViewMediator
import com.yenaly.yenaly_libs.utils.view.toggleBottomNavBehavior

class MainActivity : YenalyActivity<ActivityMainBinding, MainViewModel>() {

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val bnvMediator = BottomNavigationViewMediator(
            binding.bnvMain, binding.vpMain, listOf(
                R.id.nav_home to HomeFragment(),
                R.id.nav_slide_video to SlideVideoFragment(),
                R.id.nav_message to MessageFragment(),
                R.id.nav_personal to PersonalFragment()
            )
        ).attach()

        bnvMediator.setOnFragmentChangedListener { currentFragment ->
            when (currentFragment) {
                is HomeFragment -> binding.bnvMain.toggleBottomNavBehavior(binding.vpMain, true)
                else -> binding.bnvMain.toggleBottomNavBehavior(binding.vpMain, false)
            }
            if (currentFragment is IToggleToolbar) currentFragment.toggleToolbar(binding.toolbar)
            if (currentFragment is SlideVideoFragment) {
                binding.appBar.setBackgroundColor(Color.BLACK)
                binding.bnvMain.setBackgroundColor(Color.BLACK)
                window.setSystemBarIconLightMode(false)
            } else {
                binding.appBar.setBackgroundColor(
                    resources.getColor(
                        R.color.background_color,
                        null
                    )
                )
                binding.bnvMain.setBackgroundColor(
                    resources.getColor(
                        R.color.background_color,
                        null
                    )
                )
                if (!isAppDarkMode) {
                    binding.toolbar.setTitleTextColor(Color.BLACK)
                    binding.toolbar.setSubtitleTextColor(Color.BLACK)
                } else {
                    binding.toolbar.setTitleTextColor(Color.WHITE)
                    binding.toolbar.setSubtitleTextColor(Color.WHITE)
                }
                window.setSystemBarIconLightMode(true)
            }
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.releaseAllVideos()
    }

    fun MaterialButton.like(
        id: String,
        status: Boolean,
        likeAction: MaterialButton.() -> Unit,
        cancelLikeAction: MaterialButton.() -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.like(id, status).collect { result ->
                result.getOrNull()?.let {
                    if (status) {
                        this@like.apply(likeAction)
                        showShortToast("点赞成功")
                    } else {
                        this@like.apply(cancelLikeAction)
                        showShortToast("取消点赞成功")
                    }
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }

    fun MaterialButton.unlike(
        id: String,
        status: Boolean,
        unlikeAction: MaterialButton.() -> Unit,
        cancelUnlikeAction: MaterialButton.() -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.unlike(id, status).collect { result ->
                result.getOrNull()?.let {
                    if (status) {
                        this@unlike.apply(unlikeAction)
                        showShortToast("点踩成功")
                    } else {
                        this@unlike.apply(cancelUnlikeAction)
                        showShortToast("取消点踩成功")
                    }
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }

    fun MaterialButton.subscribe(
        id: String,
        status: Boolean,
        subscribeAction: MaterialButton.() -> Unit,
        cancelSubscribeAction: MaterialButton.() -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.subscribe(id, status).collect { result ->
                result.getOrNull()?.let {
                    if (status) {
                        this@subscribe.apply(subscribeAction)
                        showShortToast("关注成功")
                    } else {
                        this@subscribe.apply(cancelSubscribeAction)
                    }
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }
}