package com.yenaly.duanzile

import com.google.android.material.color.DynamicColors
import com.yenaly.yenaly_libs.base.YenalyApplication

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 15:05
 */
class DuanzileApplication : YenalyApplication() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}