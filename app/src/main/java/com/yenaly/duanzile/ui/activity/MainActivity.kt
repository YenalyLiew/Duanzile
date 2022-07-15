package com.yenaly.duanzile.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
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
import com.yenaly.yenaly_libs.utils.view.BottomNavigationViewMediator
import com.yenaly.yenaly_libs.utils.view.toggleBottomNavBehavior

class MainActivity : YenalyActivity<ActivityMainBinding, MainViewModel>() {

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(!isAppDarkMode)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val bnvMediator = BottomNavigationViewMediator(
            binding.bnvMain, binding.vpMain, listOf(
                R.id.nav_home to HomeFragment(),
                R.id.nav_slide_video to SlideVideoFragment(),
                R.id.nav_message to MessageFragment(),
                R.id.nav_personal to PersonalFragment()
            ), slide = false
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
                binding.toolbar.setTitleTextColor(Color.BLACK)
                binding.toolbar.setSubtitleTextColor(Color.BLACK)
                window.setSystemBarIconLightMode(!isAppDarkMode)
            }
        }
    }
}