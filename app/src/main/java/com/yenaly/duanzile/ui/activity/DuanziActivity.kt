package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.yenaly.duanzile.*
import com.yenaly.duanzile.databinding.ActivityDuanziBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.duanzile.ui.fragment.duanzi.DuanziCommentFragment
import com.yenaly.duanzile.ui.fragment.duanzi.DuanziLikeFragment
import com.yenaly.duanzile.ui.viewmodel.DuanziViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.*
import com.yenaly.yenaly_libs.utils.view.clickTrigger

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 13:20
 */
class DuanziActivity : YenalyActivity<ActivityDuanziBinding, DuanziViewModel>() {

    private val id by intentExtra<Long>(TO_DUANZI_ACTIVITY_ID)

    private val tabName = arrayOf("评论", "点赞")

    override fun setUiStyle() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background_color)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (id != null) {
            viewModel.duanziID = id!!.toString()
            viewModel.getDuanzi(viewModel.duanziID, true)
        } else {
            showShortToast("不正确的ID")
            finish()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.title = "帖子详情"
            it.setDisplayHomeAsUpEnabled(true)
        }

        binding.vpDuanzi.adapter = object : FragmentStateAdapter(this) {
            private val fragmentArray: Array<Fragment> =
                arrayOf(DuanziCommentFragment(), DuanziLikeFragment())

            override fun createFragment(position: Int): Fragment {
                return fragmentArray[position]
            }

            override fun getItemCount(): Int {
                return fragmentArray.size
            }
        }
        TabLayoutMediator(binding.tlDuanzi, binding.vpDuanzi) { tab, position ->
            tab.text = tabName[position]
        }.attach()
    }

    override fun liveDataObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.duanziFlow.collect { result ->
                result.getOrNull()?.let { data ->
                    toggleUI(data)
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

    private fun toggleUI(item: DuanziListModel.Datum) {
        binding.name.text = item.user.nickName
        binding.motto.text = item.user.signature
        binding.subscribe.isGone = item.info.isAttention
        Glide.with(this).load(item.user.avatar).circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.avatar)

        binding.content.text = item.joke.content
        if (item.joke.imageURL.isEmpty()) {
            binding.image.isGone = true
        } else {
            binding.image.isGone = false
            val contentPicDecryptUrl = item.joke.imageURL.ftpDecrypt()
            Glide.with(this).load(contentPicDecryptUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.image)
        }
        if (item.joke.videoURL.isEmpty()) {
            binding.video.isGone = true
        } else {
            binding.video.isGone = false
            val contentVideoThumbnailDecryptUrl = item.joke.thumbURL.ftpDecrypt()
            val contentVideoDecryptUrl = item.joke.videoURL.ftpDecrypt()
            binding.video.setUp(contentVideoDecryptUrl, null)
            Glide.with(this).load(contentVideoThumbnailDecryptUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.video.posterImageView)
        }
        binding.btnThumbUp.text = item.info.likeNum.toString()
        binding.btnThumbDown.text = item.info.disLikeNum.toString()
        binding.btnReply.text = item.info.commentNum.toString()
        binding.btnShare.text = item.info.shareNum.toString()
        if (item.info.isLike) {
            binding.btnThumbUp.setIconResource(R.drawable.ic_baseline_thumb_up_alt_24)
        } else {
            binding.btnThumbUp.setIconResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
        }
        if (item.info.isUnlike) {
            binding.btnThumbDown.setIconResource(R.drawable.ic_baseline_thumb_down_alt_24)
        } else {
            binding.btnThumbDown.setIconResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
        }

        initClick(item)
    }

    private fun initClick(item: DuanziListModel.Datum) {
        binding.avatar.setOnClickListener {
            startActivity<UserActivity>(TO_USER_ACTIVITY_ID to item.user.userID)
        }
        binding.btnThumbUp.clickTrigger(lifecycle) {
            if (!isLogin) {
                showShortToast("请先登录")
                return@clickTrigger
            }
            item.let {
                binding.btnThumbUp.like(
                    item.joke.jokesID.toString(),
                    !item.info.isLike,
                    likeAction = {
                        item.info.likeNum += 1
                        item.info.isLike = true
                        setIconResource(R.drawable.ic_baseline_thumb_up_alt_24)
                        text = text.toString().toLong().plus(1).toString()
                    },
                    cancelLikeAction = {
                        item.info.likeNum -= 1
                        item.info.isLike = false
                        setIconResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                        text = text.toString().toLong().minus(1).toString()
                    }
                )
            }
        }
        binding.btnThumbDown.clickTrigger(lifecycle) {
            if (!isLogin) {
                showShortToast("请先登录")
                return@clickTrigger
            }
            item.let {
                binding.btnThumbDown.unlike(
                    item.joke.jokesID.toString(),
                    !item.info.isUnlike,
                    unlikeAction = {
                        item.info.disLikeNum += 1
                        item.info.isUnlike = true
                        setIconResource(R.drawable.ic_baseline_thumb_down_alt_24)
                        text = text.toString().toLong().plus(1).toString()
                    },
                    cancelUnlikeAction = {
                        item.info.disLikeNum -= 1
                        item.info.isUnlike = false
                        setIconResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                        text = text.toString().toLong().minus(1).toString()
                    }
                )
            }
        }
        binding.subscribe.clickTrigger(lifecycle) {
            item.let {
                binding.subscribe.subscribe(
                    item.user.userID.toString(),
                    !item.info.isAttention,
                    subscribeAction = {
                        binding.subscribe.isGone = true
                        item.info.isAttention = true
                    },
                    cancelSubscribeAction = {
                        binding.subscribe.isGone = false
                        MaterialAlertDialogBuilder(context)
                            .setTitle("确定取消关注吗")
                            .setPositiveButton("确定") { _, _ ->
                                setIconResource(R.drawable.ic_baseline_add_24)
                                text = "关注"
                                item.info.isAttention = false
                                showShortToast("取关成功")
                            }
                            .setNegativeButton("取消", null)
                            .show()
                    }
                )
            }
        }
    }

    private fun MaterialButton.like(
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

    private fun MaterialButton.unlike(
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

    private fun MaterialButton.subscribe(
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

    fun MaterialButton.commentLike(
        id: String,
        status: Boolean,
        likeAction: MaterialButton.() -> Unit,
        cancelLikeAction: MaterialButton.() -> Unit
    ) {
        lifecycleScope.launchWhenStarted {
            viewModel.like(id, status).collect { result ->
                result.getOrNull()?.let {
                    if (status) {
                        this@commentLike.apply(likeAction)
                        showShortToast("点赞成功")
                    } else {
                        this@commentLike.apply(cancelLikeAction)
                        showShortToast("取消点赞成功")
                    }
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }
}