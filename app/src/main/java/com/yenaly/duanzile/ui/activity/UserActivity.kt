package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.MenuItem
import androidx.core.util.set
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.*
import com.yenaly.duanzile.databinding.ActivityUserBinding
import com.yenaly.duanzile.logic.model.UserModel
import com.yenaly.duanzile.ui.fragment.user.*
import com.yenaly.duanzile.ui.viewmodel.UserViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.*
import com.yenaly.yenaly_libs.utils.span.SpannedTextGenerator
import com.yenaly.yenaly_libs.utils.view.AppBarLayoutStateChangeListener

const val LIKED = "liked"
const val FAV = "fav"
const val TIEZI = "tiezi"
const val COMMENT = "comment"

class UserActivity : YenalyActivity<ActivityUserBinding, UserViewModel>() {

    private val id by intentExtra<Long>(TO_USER_ACTIVITY_ID)
    private val isSelf by intentExtra(TO_USER_ACTIVITY_IS_SELF, false)
    private val what by intentExtra<String>(TO_USER_ACTIVITY_WHAT)

    private val tabText = arrayOf("作品", "喜欢", "评论", "收藏")
    private val likeMap = SparseBooleanArray().apply {
        set(0, false); set(1, false); set(2, true); set(3, true)
    }

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (id != null) {
            viewModel.userID = id!!.toString()
            viewModel.getUserInfo(viewModel.userID, true)
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

        binding.tvSubscribe.setOnClickListener {
            startActivity<FollowFansActivity>(
                TO_FOLLOW_FAN_ACTIVITY_ID to viewModel.userID,
                FOLLOW_FANS_TAB to FOLLOW
            )
        }
        binding.tvFans.setOnClickListener {
            startActivity<FollowFansActivity>(
                TO_FOLLOW_FAN_ACTIVITY_ID to viewModel.userID,
                FOLLOW_FANS_TAB to FANS
            )
        }

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

        if (likeMap[data.attentionState]) {
            binding.subscribe.setIconResource(R.drawable.ic_baseline_check_24)
            binding.subscribe.text = "已关注"
        } else {
            binding.subscribe.setIconResource(R.drawable.ic_baseline_add_24)
            binding.subscribe.text = "关注"
        }

        binding.subscribe.setOnClickListener {
            subscribe(viewModel.userID, !likeMap[data.attentionState],
                subscribeAction = {
                    binding.subscribe.setIconResource(R.drawable.ic_baseline_check_24)
                    binding.subscribe.text = "已关注"
                    data.attentionState = 2
                },
                cancelSubscribeAction = {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("确定取消关注吗")
                        .setPositiveButton("确定") { _, _ ->
                            binding.subscribe.setIconResource(R.drawable.ic_baseline_add_24)
                            binding.subscribe.text = "关注"
                            data.attentionState = 0
                            showShortToast("取关成功")
                        }
                        .setNegativeButton("取消", null)
                        .show()
                }
            )
        }

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

        when (what) {
            LIKED -> binding.vpUser.setCurrentItem(1, false)
            TIEZI -> binding.vpUser.setCurrentItem(0, false)
        }

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

    fun subscribe(
        id: String,
        status: Boolean,
        subscribeAction: () -> Unit,
        cancelSubscribeAction: () -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.subscribe(id, status).collect { result ->
                result.getOrNull()?.let {
                    if (status) {
                        subscribeAction.invoke()
                        showShortToast("关注成功")
                    } else {
                        cancelSubscribeAction.invoke()
                    }
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }
}