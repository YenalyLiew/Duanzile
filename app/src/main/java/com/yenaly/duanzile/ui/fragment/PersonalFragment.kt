package com.yenaly.duanzile.ui.fragment

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.FragmentPersonalBinding
import com.yenaly.duanzile.ui.activity.IToggleToolbar
import com.yenaly.duanzile.ui.activity.MainActivity
import com.yenaly.duanzile.ui.viewmodel.PersonalViewModel
import com.yenaly.yenaly_libs.base.YenalyFragment

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 20:10
 */
class PersonalFragment : YenalyFragment<FragmentPersonalBinding, PersonalViewModel>(),
    IToggleToolbar {

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun toggleToolbar(toolbar: Toolbar) {
        toolbar.title = "我的"
        toolbar.setSubtitle(R.string.app_name)
    }
}