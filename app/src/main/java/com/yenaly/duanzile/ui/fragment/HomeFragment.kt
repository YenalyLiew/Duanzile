package com.yenaly.duanzile.ui.fragment

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.FragmentHomeBinding
import com.yenaly.duanzile.ui.activity.IToggleToolbar
import com.yenaly.duanzile.ui.activity.MainActivity
import com.yenaly.duanzile.ui.fragment.home.AllPicFragment
import com.yenaly.duanzile.ui.fragment.home.AllTextFragment
import com.yenaly.duanzile.ui.fragment.home.LatestFragment
import com.yenaly.duanzile.ui.fragment.home.RecommendFragment
import com.yenaly.duanzile.ui.viewmodel.HomeViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 18:23
 */
class HomeFragment : YenalyFragment<FragmentHomeBinding, HomeViewModel>(), IToggleToolbar {

    private val tabNameArray = arrayOf("推荐", "新鲜", "纯文", "趣图")
    private val tabFragmentArray =
        arrayOf(RecommendFragment(), LatestFragment(), AllTextFragment(), AllPicFragment())

    override fun initData(savedInstanceState: Bundle?) {
        check(tabFragmentArray.size == tabNameArray.size) {
            "name and fragment must be ONE TO ONE CORRESPONDENCE."
        }

        binding.vpHome.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount() = tabFragmentArray.size

            override fun createFragment(position: Int): Fragment {
                return tabFragmentArray[position]
            }
        }

        TabLayoutMediator(binding.tlHome, binding.vpHome) { tab, position ->
            tab.text = tabNameArray[position]
        }.attach()
    }

    override fun toggleToolbar(toolbar: Toolbar) {
        toolbar.title = "主页"
        toolbar.setSubtitle(R.string.app_name)
    }
}