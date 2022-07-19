package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.logic.model.LoginUserModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 18:08
 */
open class MainViewModel(application: Application) : BaseViewModel(application) {
    private val _currentUserInfoFlow =
        MutableSharedFlow<Result<LoginUserModel.Data>>(replay = 1)
    val currentUserInfoFlow = _currentUserInfoFlow.asSharedFlow()

    fun getCurrentUserInfo(single: Boolean) {
        if (single) {
            viewModelScope.singleLaunch(0) {
                NetworkRepo.getLoginUserInfo().collect(_currentUserInfoFlow::emit)
            }
        } else {
            viewModelScope.launch {
                NetworkRepo.getLoginUserInfo().collect(_currentUserInfoFlow::emit)
            }
        }
    }
}