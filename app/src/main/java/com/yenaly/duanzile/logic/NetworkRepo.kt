package com.yenaly.duanzile.logic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import androidx.paging.toLiveData
import com.yenaly.duanzile.logic.network.DuanzileNetwork
import com.yenaly.duanzile.logic.network.DuanziPagingSource

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 17:00
 */
object NetworkRepo {

    private const val PAGE_SIZE = 15

    fun getHomeRecommend() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource {
                DuanzileNetwork.homeService.getRecommend()
            }
        }
    ).flow

    fun getHomeLatest() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource {
                DuanzileNetwork.homeService.getLatest()
            }
        }
    ).flow

    fun getHomeAllPic() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource {
                DuanzileNetwork.homeService.getAllPic()
            }
        }
    ).flow

    fun getHomeAllText() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource {
                DuanzileNetwork.homeService.getAllText()
            }
        }
    ).flow

    fun getSlideVideo() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource {
                DuanzileNetwork.slideVideoService.getSlideVideo()
            }
        }
    ).flow
}