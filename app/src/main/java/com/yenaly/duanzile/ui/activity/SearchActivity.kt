package com.yenaly.duanzile.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.yenaly.duanzile.databinding.ActivitySearchBinding
import com.yenaly.duanzile.ui.adapter.DuanziRvAdapter
import com.yenaly.duanzile.ui.viewmodel.SearchViewModel
import com.yenaly.yenaly_libs.base.YenalyActivity
import com.yenaly.yenaly_libs.utils.SystemStatusUtil
import com.yenaly.yenaly_libs.utils.setSystemBarIconLightMode
import com.yenaly.yenaly_libs.utils.unsafeLazy
import com.yenaly.yenaly_libs.utils.view.hideIme
import com.yenaly.yenaly_libs.utils.view.textString
import kotlinx.coroutines.flow.collectLatest

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/24 024 14:40
 */
class SearchActivity : YenalyActivity<ActivitySearchBinding, SearchViewModel>() {

    private val adapter by unsafeLazy { DuanziRvAdapter() }

    override fun setUiStyle() {
        SystemStatusUtil.fullScreen(window, true)
        window.setSystemBarIconLightMode(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
        }
        binding.rvLayout.rvHome.adapter = adapter
        binding.rvLayout.srlHome.setOnRefreshListener {
            searchDuanzi(binding.etSearch.textString())
        }
        binding.etSearch.setOnEditorActionListener { v, _, _ ->
            searchDuanzi(v.text.toString())
            binding.etSearch.hideIme(window)
            return@setOnEditorActionListener true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun liveDataObserve() {
        lifecycleScope.launchWhenStarted {
            adapter.loadStateFlow.collect { state ->
                binding.rvLayout.srlHome.isRefreshing = state.source.refresh is LoadState.Loading
            }
        }
    }

    private fun searchDuanzi(keyword: String) {
        lifecycleScope.launchWhenStarted {
            viewModel.searchDuanzi(keyword).collectLatest(adapter::submitData)
        }
    }
}