package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.TO_FOLLOW_FAN_ACTIVITY_ID
import com.yenaly.duanzile.databinding.ActivityFollowFansBinding
import com.yenaly.duanzile.ui.fragment.user.FANS
import com.yenaly.duanzile.ui.fragment.user.FOLLOW
import com.yenaly.duanzile.ui.fragment.user.FollowFansFragment
import com.yenaly.duanzile.ui.viewmodel.FollowFansViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.*

const val FOLLOW_FANS_ARGUMENT = "follow_fans"
const val FOLLOW_FANS_TAB = "tab"

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 16:25
 */
class FollowFansActivity : YenalyActivity<ActivityFollowFansBinding, FollowFansViewModel>() {

    private val id by intentExtra<String>(TO_FOLLOW_FAN_ACTIVITY_ID)

    private val tabName = arrayOf("关注", "粉丝")

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (id != null) {
            viewModel.userID = id!!.toString()
        } else {
            showShortToast("不正确的ID")
            finish()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.title = "关注与粉丝"
            it.setDisplayHomeAsUpEnabled(true)
        }

        followFansImpl()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun followFansImpl() {
        binding.vpFans.adapter = object : FragmentStateAdapter(this) {

            private val fragmentArray = arrayOf(
                FollowFansFragment().makeBundle(FOLLOW_FANS_ARGUMENT to FOLLOW),
                FollowFansFragment().makeBundle(FOLLOW_FANS_ARGUMENT to FANS)
            )

            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }

            override fun getItemCount(): Int {
                return fragmentArray.size
            }
        }

        TabLayoutMediator(binding.tlFans, binding.vpFans) { tab, position ->
            tab.text = tabName[position]
        }.attach()

        if (FOLLOW_FANS_TAB == FOLLOW) {
            binding.vpFans.setCurrentItem(0, false)
        } else {
            binding.vpFans.setCurrentItem(1, false)
        }
    }
}