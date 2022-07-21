package com.yenaly.duanzile.logic.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yenaly.duanzile.logic.model.LikeUserModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 15:13
 */
class LikeUserPagingSource(private val id: String) : PagingSource<Int, LikeUserModel.Data>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LikeUserModel.Data> {
        return try {
            val page = params.key ?: 1
            val response = DuanzileNetwork.duanziService.getLikeUser(id, page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (response.data.isNotEmpty()) page + 1 else null
            LoadResult.Page(response.data, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LikeUserModel.Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}