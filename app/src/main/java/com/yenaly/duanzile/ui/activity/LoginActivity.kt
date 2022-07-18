package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.ActivityLoginBinding
import com.yenaly.duanzile.login
import com.yenaly.duanzile.ui.viewmodel.LoginViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.SystemStatusUtil
import com.yenaly.yenaly_libs.utils.setSystemBarIconLightMode
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.view.clickTrigger
import com.yenaly.yenaly_libs.utils.view.hideIme
import com.yenaly.yenaly_libs.utils.view.textString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 10:18
 */
class LoginActivity : YenalyActivity<ActivityLoginBinding, LoginViewModel>() {

    private val isLoginByPhone get() = binding.btnGetVerifyCode.isVisible

    private val phoneNum get() = binding.etPhone.textString()
    private val password get() = binding.etPsw.textString()


    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initClick()
    }

    override fun liveDataObserve() {
        lifecycleScope.launchWhenStarted {
            viewModel.pswFlow
                .collect { result ->
                    val userInfo = result.getOrNull()
                    userInfo?.let {
                        showShortToast("登录成功")
                        setResult(RESULT_OK)
                        login(it.token)
                        finish()
                    } ?: result.exceptionOrNull()?.let { e ->
                        e.printStackTrace()
                        showShortToast(e.message)
                    }
                    hideLoadingDialog()
                }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.codeFlow
                .collect { result ->
                    val userInfo = result.getOrNull()
                    userInfo?.let {
                        showShortToast("登录成功")
                        setResult(RESULT_OK)
                        login(it.token)
                        finish()
                    } ?: result.exceptionOrNull()?.let { e ->
                        e.printStackTrace()
                        showShortToast(e.message)
                    }
                    hideLoadingDialog()
                }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.getCodeFlow.collect { result ->
                Log.d("result", result.toString())
                val code = result.getOrNull()
                code?.let {
                    showShortToast("发送验证码成功，注意查收")
                } ?: result.exceptionOrNull()?.let { e ->
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

    private fun initClick() {
        binding.btnToggleLoginWay.clickTrigger(lifecycle, 1000) {
            if (isLoginByPhone) {
                binding.btnToggleLoginWay.text = "验证码方式"
                binding.btnGetVerifyCode.isVisible = false
                binding.tilPsw.setStartIconDrawable(R.drawable.ic_baseline_password_24)
                binding.tilPsw.hint = "密码"
                binding.etPsw.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                binding.btnToggleLoginWay.text = "密码方式"
                binding.btnGetVerifyCode.isVisible = true
                binding.tilPsw.setStartIconDrawable(R.drawable.ic_baseline_code_24)
                binding.tilPsw.hint = "验证码"
                binding.etPsw.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }

        binding.btnLogin.clickTrigger(lifecycle, 1000) {
            if (phoneNum.isEmpty() && password.isEmpty()) {
                showShortToast("请完整填写手机号和密码或验证码")
                return@clickTrigger
            }
            if (isLoginByPhone) {
                viewModel.loginByVerifyCode(phoneNum, password)
            } else {
                viewModel.loginByPassword(phoneNum, password)
            }
            binding.etPhone.hideIme(window)
            binding.etPsw.hideIme(window)
            showLoadingDialog()
        }

        binding.btnGetVerifyCode.setOnClickListener {
            if (phoneNum.isEmpty() && password.isEmpty()) {
                showShortToast("请完整填写手机号和密码或验证码")
                return@setOnClickListener
            }
            viewModel.getVerifyCode(phoneNum)
            getCodeCountdown()
        }
    }

    private fun getCodeCountdown() {
        flow {
            for (i in 60 downTo 1) {
                emit(i)
                delay(1000)
            }
            emit(0)
        }.flowOn(
            Dispatchers.Main
        ).onStart {
            binding.btnGetVerifyCode.isEnabled = false
        }.onEach {
            binding.btnGetVerifyCode.text = "${it}s"
        }.onCompletion {
            binding.btnGetVerifyCode.isEnabled = true
            binding.btnGetVerifyCode.setText(R.string.get_verify_code)
        }.launchIn(lifecycleScope)
    }
}