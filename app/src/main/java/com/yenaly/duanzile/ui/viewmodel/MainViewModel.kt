package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.logic.model.LoginUserModel
import com.yenaly.duanzile.logic.model.UserModel
import com.yenaly.yenaly_libs.base.YenalyViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 18:08
 */
open class MainViewModel(application: Application) : YenalyViewModel(application) {
    fun like(id: String, status: Boolean) =
        NetworkRepo.like(id, status)

    fun unlike(id: String, status: Boolean) =
        NetworkRepo.unlike(id, status)

    private val _currentUserInfoFlow =
        MutableSharedFlow<Result<UserModel.Data>>()
    val currentUserInfoFlow = _currentUserInfoFlow.asSharedFlow()

    fun getCurrentUserInfo() {
        viewModelScope.launch {
            NetworkRepo.getCurrentUserInfo().collect(_currentUserInfoFlow::emit)
        }
    }
}