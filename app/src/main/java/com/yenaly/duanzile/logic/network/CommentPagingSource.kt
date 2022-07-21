package com.yenaly.duanzile.logic.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yenaly.duanzile.logic.model.CommentModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 15:07
 */
class CommentPagingSource(private val id: String) : PagingSource<Int, CommentModel.Data.Comment>() {
    override fun getRefreshKey(state: PagingState<Int, CommentModel.Data.Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentModel.Data.Comment> {
        return try {
            val page = params.key ?: 1
            val response = DuanzileNetwork.duanziService.getComment(id, page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (response.data.comments.isNotEmpty()) page + 1 else null
            LoadResult.Page(response.data.comments, prevKey, nextKey)
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}