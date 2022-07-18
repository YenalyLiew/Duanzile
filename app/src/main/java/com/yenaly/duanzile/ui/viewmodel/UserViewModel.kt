package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.logic.model.LoginUserModel
import com.yenaly.duanzile.logic.model.UserModel
import com.yenaly.yenaly_libs.base.YenalyViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 21:20
 */
class UserViewModel(application: Application) : YenalyViewModel(application) {

    lateinit var userID: String

    private val _loginUserFlow =
        MutableSharedFlow<Result<LoginUserModel.Data>>()
    val loginUserFlow = _loginUserFlow.asSharedFlow()

    private val _userFlow =
        MutableSharedFlow<Result<UserModel.Data>>(replay = 1)
    val userFlow = _userFlow.asSharedFlow()

    fun getLoginUserInfo() {
        viewModelScope.launch {
            NetworkRepo.getLoginUserInfo().collect(_loginUserFlow::emit)
        }
    }

    fun getSingleUserInfo(id: String) {
        viewModelScope.singleLaunch(0) {
            NetworkRepo.getUserInfo(id).collect(_userFlow::emit)
        }
    }

    fun getUserInfo(id: String) {
        viewModelScope.launch {
            NetworkRepo.getUserInfo(id).collect(_userFlow::emit)
        }
    }

    fun getUserDuanzi(id: String) =
        NetworkRepo.getUserDuanzi(id).cachedIn(viewModelScope)

    fun getUserDuanziVideo(id: String) =
        NetworkRepo.getUserDuanziVideo(id).cachedIn(viewModelScope)

    fun getUserLikedDuanzi(id: String) =
        NetworkRepo.getUserLikedDuanzi(id).cachedIn(viewModelScope)

    fun getUserLikedDuanziVideo(id: String) =
        NetworkRepo.getUserLikedDuanziVideo(id).cachedIn(viewModelScope)
}