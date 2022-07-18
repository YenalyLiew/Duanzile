package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.TO_TEXT_VIDEO_SPLIT_FRAGMENT
import com.yenaly.duanzile.TO_USER_ACTIVITY_ID
import com.yenaly.duanzile.TO_USER_ACTIVITY_IS_SELF
import com.yenaly.duanzile.databinding.ActivityUserBinding
import com.yenaly.duanzile.logic.model.UserModel
import com.yenaly.duanzile.ui.fragment.user.LIKE
import com.yenaly.duanzile.ui.fragment.user.TextVideoSplitFragment
import com.yenaly.duanzile.ui.fragment.user.WORK
import com.yenaly.duanzile.ui.viewmodel.UserViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.*
import com.yenaly.yenaly_libs.utils.span.SpannedTextGenerator
import com.yenaly.yenaly_libs.utils.view.AppBarLayoutStateChangeListener

class UserActivity : YenalyActivity<ActivityUserBinding, UserViewModel>() {

    private val id by intentExtra<Long>(TO_USER_ACTIVITY_ID)
    private val isSelf by intentExtra(TO_USER_ACTIVITY_IS_SELF, false)

    private val tabText = arrayOf("作品", "喜欢", "评论", "收藏")

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (id != null) {
            viewModel.userID = id!!.toString()
            viewModel.getSingleUserInfo(viewModel.userID)
        } else {
            showShortToast("不正确的ID")
            finish()
        }

        binding.subscribe.isGone = isSelf
        binding.dm.isGone = isSelf
        binding.editInfo.isGone = !isSelf

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayoutStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                supportActionBar?.setDisplayShowTitleEnabled(state == State.COLLAPSED)
            }
        })


        initNum()

        initTab()
    }

    override fun liveDataObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.userFlow.collect { result ->
                result.getOrNull()?.let { data ->
                    toggleUserInfo(data)
                    Log.d("data", data.toString())
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
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

    private fun toggleUserInfo(data: UserModel.Data) {

        supportActionBar?.title = data.nickname

        SpannedTextGenerator.KotlinBuilder()
            .addText(data.likeNum, isBold = true, isNewLine = false)
            .addText(" 获赞", isNewLine = false)
            .showIn(binding.tvLike)
        SpannedTextGenerator.KotlinBuilder()
            .addText(data.fansNum, isBold = true, isNewLine = false)
            .addText(" 粉丝", isNewLine = false)
            .showIn(binding.tvFans)
        SpannedTextGenerator.KotlinBuilder()
            .addText(data.attentionNum, isBold = true, isNewLine = false)
            .addText(" 关注", isNewLine = false)
            .showIn(binding.tvSubscribe)

        binding.name.text = data.nickname
        binding.motto.text = data.signature
        binding.enterTime.text = "入驻段子乐：${data.joinTime}"

        Glide.with(this@UserActivity).load(data.avatar)
            .transition(DrawableTransitionOptions.withCrossFade())
            .circleCrop()
            .into(binding.ivUser)
        Glide.with(this@UserActivity).load(data.avatar)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.ivUserBig)
    }

    private fun initNum() {
        SpannedTextGenerator.KotlinBuilder()
            .addText("0", isBold = true, isNewLine = false)
            .addText(" 获赞", isNewLine = false)
            .showIn(binding.tvLike)
        SpannedTextGenerator.KotlinBuilder()
            .addText("0", isBold = true, isNewLine = false)
            .addText(" 粉丝", isNewLine = false)
            .showIn(binding.tvFans)
        SpannedTextGenerator.KotlinBuilder()
            .addText("0", isBold = true, isNewLine = false)
            .addText(" 关注", isNewLine = false)
            .showIn(binding.tvSubscribe)
    }

    private fun initTab() {
        binding.vpUser.adapter = object : FragmentStateAdapter(this) {

            private val fragmentArray: Array<Fragment> =
                arrayOf(
                    TextVideoSplitFragment().makeBundle(TO_TEXT_VIDEO_SPLIT_FRAGMENT to WORK),
                    TextVideoSplitFragment().makeBundle(TO_TEXT_VIDEO_SPLIT_FRAGMENT to LIKE)
                )

            override fun getItemCount(): Int {
                return fragmentArray.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }
        }

        TabLayoutMediator(binding.tlUser, binding.vpUser) { tab, position ->
            tab.text = tabText[position]
        }.attach()
    }
}