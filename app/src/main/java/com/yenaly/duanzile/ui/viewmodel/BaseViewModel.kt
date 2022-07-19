package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.yenaly_libs.base.YenalyViewModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/19 019 13:34
 */
open class BaseViewModel(application: Application) : YenalyViewModel(application) {
    fun like(id: String, status: Boolean) =
        NetworkRepo.like(id, status)

    fun unlike(id: String, status: Boolean) =
        NetworkRepo.unlike(id, status)

    fun subscribe(id: String, subscribe: Boolean) = if (subscribe) {
        NetworkRepo.subscribe("1", id)
    } else NetworkRepo.subscribe("0", id)
}