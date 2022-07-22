package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import com.yenaly.duanzile.logic.NetworkRepo

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 16:27
 */
class FollowFansViewModel(application: Application) : BaseViewModel(application) {

    lateinit var userID: String

    fun getUserSubscribeList(id: String) = NetworkRepo.getUserSubscribeList(id)

    fun getUserFanList(id: String) = NetworkRepo.getUserFanList(id)
}