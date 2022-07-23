package com.yenaly.duanzile.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import cn.jzvd.JzvdStd

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 23:54
 */
class CustomJzvdStd @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : JzvdStd(context, attrs) {
    override fun init(context: Context?) {
        super.init(context)
        findViewById<View>(cn.jzvd.R.id.layout_top).background = null
        posterImageView.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}