package com.yenaly.duanzile.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yenaly.duanzile.*
import com.yenaly.duanzile.databinding.FragmentPersonalBinding
import com.yenaly.duanzile.logic.model.LoginUserModel
import com.yenaly.duanzile.ui.activity.*
import com.yenaly.duanzile.ui.fragment.user.FANS
import com.yenaly.duanzile.ui.fragment.user.FOLLOW
import com.yenaly.duanzile.ui.viewmodel.main.PersonalViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment
import com.yenaly.yenaly_libs.utils.*
import com.yenaly.yenaly_libs.utils.span.SpannedTextGenerator
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 20:10
 */
class PersonalFragment : YenalyFragment<FragmentPersonalBinding, PersonalViewModel>(),
    IToggleToolbar {

    private val clickViewArray by unsafeLazy {
        arrayOf(
            binding.child1, binding.fans, binding.subscribe,
            binding.ledou, binding.tiezi, binding.comment,
            binding.liked, binding.fav
        )
    }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.getCurrentUserInfo(false)
            }
        }

    override fun initData(savedInstanceState: Bundle?) {
        if (!isLogin) {
            Glide.with(requireContext()).load(R.mipmap.ic_launcher).circleCrop()
                .into(binding.avatar)
            binding.name.text = "请登录"
            binding.motto.text = "点击此处登录后体验更多精彩"
            toggleUserInfo("-", "-", "-")
            clickViewArray.forEach { view ->
                view.setOnClickListener {
                    showShortToast("请先登录哦")
                    val intent = Intent(activity, LoginActivity::class.java)
                    loginLauncher.launch(intent)
                }
            }
        }

        binding.giveMeThumbUp.setOnClickListener {
            browse("https://github.com/YenalyLiew/Duanzile")
        }
        binding.settings.setOnClickListener {
            showShortToast("没做")
        }
    }

    override fun liveDataObserve() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.currentUserInfoFlow.collect { result ->
                result.getOrNull()?.let { data ->

                    initAfterLoginClick(data)

                    Glide.with(requireContext()).load(data.user.avatar.ftpDecrypt())
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.avatar)
                    binding.name.text = data.user.nickname
                    binding.motto.text = data.user.signature

                    toggleUserInfo(
                        data.info.attentionNum.toString(),
                        data.info.fansNum.toString(),
                        "0" // fake ledou number.
                    )
                } ?: result.exceptionOrNull()?.let { e ->
                    e.printStackTrace()
                    showShortToast(e.message)
                }
            }
        }
    }

    override fun toggleToolbar(toolbar: Toolbar) {
        toolbar.title = "我的"
        toolbar.setSubtitle(R.string.app_name)
    }

    override fun onResume() {
        super.onResume()
        if (isLogin) {
            viewModel.getCurrentUserInfo(false)
        }
    }

    private fun initAfterLoginClick(data: LoginUserModel.Data) {
        binding.child1.setOnClickListener {
            startActivity<UserActivity>(
                values = arrayOf<Pair<String, Any>>(
                    TO_USER_ACTIVITY_ID to data.user.userID,
                    TO_USER_ACTIVITY_IS_SELF to true
                )
            )
        }

        binding.subscribe.setOnClickListener {
            requireActivity().startActivity<FollowFansActivity>(
                TO_FOLLOW_FAN_ACTIVITY_ID to data.user.userID.toString(),
                FOLLOW_FANS_TAB to FOLLOW
            )
        }
        binding.fans.setOnClickListener {
            requireActivity().startActivity<FollowFansActivity>(
                TO_FOLLOW_FAN_ACTIVITY_ID to data.user.userID.toString(),
                FOLLOW_FANS_TAB to FANS
            )
        }
        binding.ledou.setOnClickListener {
            showShortToast("没做")
        }
        binding.tiezi.setOnClickListener {
            startActivity<UserActivity>(
                values = arrayOf<Pair<String, Any>>(
                    TO_USER_ACTIVITY_ID to data.user.userID,
                    TO_USER_ACTIVITY_IS_SELF to true,
                    TO_USER_ACTIVITY_WHAT to TIEZI
                )
            )
        }
        binding.comment.setOnClickListener {
            showShortToast("没做")
        }
        binding.liked.setOnClickListener {
            startActivity<UserActivity>(
                values = arrayOf<Pair<String, Any>>(
                    TO_USER_ACTIVITY_ID to data.user.userID,
                    TO_USER_ACTIVITY_IS_SELF to true,
                    TO_USER_ACTIVITY_WHAT to LIKED
                )
            )
        }
        binding.fav.setOnClickListener {
            showShortToast("没做")
        }


    }

    private fun toggleUserInfo(subscribeNum: String, fanNum: String, ledouNum: String) {
        SpannedTextGenerator.KotlinBuilder()
            .addText(subscribeNum, textSize = 18.sp, isBold = true)
            .addText("关注", isNewLine = false)
            .showIn(binding.subscribe)
        SpannedTextGenerator.KotlinBuilder()
            .addText(fanNum, textSize = 18.sp, isBold = true)
            .addText("粉丝", isNewLine = false)
            .showIn(binding.fans)
        SpannedTextGenerator.KotlinBuilder()
            .addText(ledouNum, textSize = 18.sp, isBold = true)
            .addText("乐豆", isNewLine = false)
            .showIn(binding.ledou)
    }
}