package com.yenaly.duanzile.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.view.isGone
import cn.jzvd.JzvdStd

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/15 015 19:56
 */
class SlideVideoJzvdStd @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : JzvdStd(context, attrs) {
    override fun init(context: Context?) {
        super.init(context)
        findViewById<View>(cn.jzvd.R.id.layout_top).background = null
        findViewById<View>(cn.jzvd.R.id.layout_bottom).isGone = true
        // findViewById<View>(cn.jzvd.R.id.surface_container)
        posterImageView.scaleType = ImageView.ScaleType.FIT_CENTER
    }
}