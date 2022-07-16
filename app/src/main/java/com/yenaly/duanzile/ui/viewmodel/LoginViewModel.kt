package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.logic.model.LoginUserModel
import com.yenaly.yenaly_libs.base.YenalyViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 10:32
 */
class LoginViewModel(application: Application) : YenalyViewModel(application) {
    private val _pswFlow =
        MutableSharedFlow<Result<LoginUserModel.Data>>()
    val pswFlow = _pswFlow.asSharedFlow()

    private val _codeFlow =
        MutableSharedFlow<Result<LoginUserModel.Data>>()
    val codeFlow = _codeFlow.asSharedFlow()

    private val _getCodeFlow =
        MutableSharedFlow<Result<String>>()
    val getCodeFlow = _getCodeFlow.asSharedFlow()

    fun loginByPassword(phone: String, password: String) {
        viewModelScope.launch {
            NetworkRepo.loginByPassword(phone, password).collect {
                _pswFlow.emit(it)
            }
        }
    }

    fun loginByVerifyCode(phone: String, code: String) {
        viewModelScope.launch {
            NetworkRepo.loginByVerifyCode(phone, code).collect {
                _codeFlow.emit(it)
            }
        }
    }

    fun getVerifyCode(phone: String) {
        viewModelScope.launch {
            NetworkRepo.getLoginVerifyCode(phone).collect {
                _getCodeFlow.emit(it)
            }
        }
    }
}