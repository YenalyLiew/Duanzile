package com.yenaly.yenaly_libs.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yenaly.yenaly_libs.utils.SingleFlowLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @ProjectName : YenalyModule
 * @Author : Yenaly Liew
 * @Time : 2022/04/20 020 11:37
 * @Description : Description...
 */
open class YenalyViewModel(application: Application) : AndroidViewModel(application) {

    private val singleFlowLaunch = SingleFlowLaunch(viewModelScope)

    @Suppress("unused")
    protected fun CoroutineScope.singleLaunch(
        tag: Any,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) = singleFlowLaunch.singleLaunch(tag, context, start, block)
}