package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.yenaly.duanzile.R
import com.yenaly.duanzile.TO_PIC_VIEW_URLS
import com.yenaly.duanzile.databinding.ActivityPicViewBinding
import com.yenaly.duanzile.ui.adapter.PicViewVpAdapter
import com.yenaly.duanzile.ui.viewmodel.PicViewViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.SystemStatusUtil
import com.yenaly.yenaly_libs.utils.intentExtra
import com.yenaly.yenaly_libs.utils.statusBarHeight

/**
 * @ProjectName : BlViewer
 * @Author : Yenaly Liew
 * @Time : 2022/06/01 001 22:59
 * @Description : Description...
 */
class PicViewActivity : YenalyActivity<ActivityPicViewBinding, PicViewViewModel>() {

    private lateinit var picViewAdapter: PicViewVpAdapter
    private val position = 0
    private val urlsList by intentExtra(TO_PIC_VIEW_URLS, emptyList<String>())

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        picViewAdapter = PicViewVpAdapter(urlsList)

        binding.pvaViewPager.adapter = picViewAdapter
        binding.pvaViewPager.setCurrentItem(position, false)
        val emptyLayoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight)
        binding.emptyView.layoutParams = emptyLayoutParams

        val totalPage = urlsList.size
        binding.picPosition.text = getString(R.string.pic_view_position, position + 1, totalPage)
        binding.pvaViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.picPosition.text =
                        getString(R.string.pic_view_position, position + 1, totalPage)
                }
            }
        )

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onNavigateUp(): Boolean {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        return super.onNavigateUp()
    }
}