package com.yenaly.duanzile.logic.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yenaly.duanzile.logic.model.SubscribeUserModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 21:40
 */
class FollowFansPagingSource(
    private val service: suspend (Int) -> SubscribeUserModel
) : PagingSource<Int, SubscribeUserModel.Datum>() {

    override fun getRefreshKey(state: PagingState<Int, SubscribeUserModel.Datum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SubscribeUserModel.Datum> {
        return try {
            val page = params.key ?: 1
            val response = service.invoke(page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (response.data.isNotEmpty()) page + 1 else null
            LoadResult.Page(response.data, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}