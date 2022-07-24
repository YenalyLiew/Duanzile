package com.yenaly.duanzile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yenaly.duanzile.logic.NetworkRepo

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/24 024 14:40
 */
class SearchViewModel(application: Application) : BaseViewModel(application) {
    fun searchDuanzi(keyword: String) = NetworkRepo.searchDuanzi(keyword).cachedIn(viewModelScope)
}