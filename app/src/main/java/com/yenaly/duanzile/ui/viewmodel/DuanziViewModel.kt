package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.logic.model.DuanziListModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 13:21
 */
class DuanziViewModel(application: Application) : BaseViewModel(application) {
    lateinit var duanziID: String

    private val _duanziFlow =
        MutableSharedFlow<Result<DuanziListModel.Datum>>(replay = 1)
    val duanziFlow = _duanziFlow.asSharedFlow()

    fun commentLike(id: String, status: Boolean) = NetworkRepo.commentLike(id, status)

    fun getComment(id: String) =
        NetworkRepo.getComment(id).cachedIn(viewModelScope)

    fun getLikeUser(id: String) =
        NetworkRepo.getLikeUser(id).cachedIn(viewModelScope)

    fun getDuanzi(id: String, single: Boolean) {
        if (single) {
            viewModelScope.singleLaunch(0) {
                NetworkRepo.getDuanzi(id).collect(_duanziFlow::emit)
            }
        } else {
            viewModelScope.launch {
                NetworkRepo.getDuanzi(id).collect(_duanziFlow::emit)
            }
        }
    }
}