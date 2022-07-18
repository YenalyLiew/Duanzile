package com.yenaly.duanzile.logic

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.yenaly.duanzile.logic.model.IDuanzileModel
import com.yenaly.duanzile.logic.network.DuanziPagingSource
import com.yenaly.duanzile.logic.network.DuanzileNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

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

    fun loginByPassword(phone: String, password: String) = makeRequest(
        service = { DuanzileNetwork.loginService.loginByPassword(phone, password) },
        success = { it.data }
    )

    fun loginByVerifyCode(phone: String, code: String) = makeRequest(
        service = { DuanzileNetwork.loginService.loginByVerifyCode(phone, code) },
        success = { it.data }
    )

    fun getLoginVerifyCode(phone: String) = makeRequest(
        service = { DuanzileNetwork.loginService.getLoginVerifyCode(phone) },
        success = { it.msg }
    )

    fun like(id: String, status: Boolean) = makeRequest(
        service = { DuanzileNetwork.generalService.like(id, status.toString()) },
        success = { it.msg }
    )

    fun unlike(id: String, status: Boolean) = makeRequest(
        service = { DuanzileNetwork.generalService.unlike(id, status.toString()) },
        success = { it.msg }
    )

    fun getLoginUserInfo() = makeRequest(
        service = { DuanzileNetwork.userService.getLoginUserInfo() },
        success = { it.data }
    )

    fun getUserInfo(id: String) = makeRequest(
        service = { DuanzileNetwork.userService.getUserInfo(id) },
        success = { it.data }
    )

    fun getUserDuanzi(id: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource { page ->
                DuanzileNetwork.userService.getUserDuanzi(id, page)
            }
        }
    ).flow

    fun getUserDuanziVideo(id: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource { page ->
                DuanzileNetwork.userService.getUserDuanziVideo(id, page)
            }
        }
    ).flow

    fun getUserLikedDuanzi(id: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource { page ->
                DuanzileNetwork.userService.getUserLikedDuanzi(id, page)
            }
        }
    ).flow

    fun getUserLikedDuanziVideo(id: String) = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = {
            DuanziPagingSource { page ->
                DuanzileNetwork.userService.getUserLikedDuanziVideo(id, page)
            }
        }
    ).flow

    private fun <T : IDuanzileModel, R> makeRequest(
        service: suspend () -> T,
        success: (T) -> R
    ) = flow {
        val result = try {
            val response = service.invoke()
            if (response.code == 200) {
                Result.success(success.invoke(response))
            } else {
                Result.failure(IllegalStateException(response.msg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)
}