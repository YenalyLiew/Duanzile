package com.yenaly.duanzile.ui.viewmodel.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.ui.viewmodel.MainViewModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 15:27
 */
class HomeViewModel(application: Application) : MainViewModel(application) {

    fun getRecommend() =
        NetworkRepo.getHomeRecommend().cachedIn(viewModelScope)

    fun getLatest() =
        NetworkRepo.getHomeLatest().cachedIn(viewModelScope)

    fun getAllText() =
        NetworkRepo.getHomeAllText().cachedIn(viewModelScope)

    fun getAllPic() =
        NetworkRepo.getHomeAllPic().cachedIn(viewModelScope)
}