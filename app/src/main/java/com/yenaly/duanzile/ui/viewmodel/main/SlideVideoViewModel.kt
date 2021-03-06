package com.yenaly.duanzile.ui.viewmodel.main

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.yenaly.duanzile.logic.NetworkRepo
import com.yenaly.duanzile.ui.viewmodel.MainViewModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/15 015 15:56
 */
class SlideVideoViewModel(application: Application) : MainViewModel(application) {
    fun getSlideVideo() =
        NetworkRepo.getSlideVideo().cachedIn(viewModelScope)
}