package com.yenaly.duanzile.ui.fragment.user

import android.os.Bundle
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.TO_TEXT_VIDEO_SPLIT_FRAGMENT
import com.yenaly.duanzile.databinding.FragmentTextVideoSplitBinding
import com.yenaly.duanzile.ui.viewmodel.UserViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.arguments
import com.yenaly.yenaly_libs.utils.makeBundle

const val WORK = 0
const val LIKE = 1

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/17 017 14:16
 */
class TextVideoSplitFragment : YenalyFragment<FragmentTextVideoSplitBinding, UserViewModel>() {

    private val type by arguments(TO_TEXT_VIDEO_SPLIT_FRAGMENT, WORK)

    private val tabName = arrayOf("文字·图片", "视频")

    override fun initData(savedInstanceState: Bundle?) {
        binding.vp.adapter = object : FragmentStateAdapter(this) {

            private val fragmentArray: Array<Fragment> = arrayOf(
                TextPhotoItemFragment().makeBundle(TO_TEXT_VIDEO_SPLIT_FRAGMENT to type),
                VideoItemFragment().makeBundle(TO_TEXT_VIDEO_SPLIT_FRAGMENT to type)
            )

            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }

            override fun getItemCount(): Int {
                return fragmentArray.size
            }
        }

        TabLayoutMediator(binding.tl, binding.vp) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }
}