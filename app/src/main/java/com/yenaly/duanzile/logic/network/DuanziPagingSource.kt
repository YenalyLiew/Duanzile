package com.yenaly.duanzile.logic.network

import android.util.Base64
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.yenaly_libs.utils.decodeToByteArrayByBase64

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 17:08
 */
class DuanziPagingSource(
    private val service: suspend () -> DuanziListModel
) : PagingSource<Int, DuanziListModel.Datum>() {

    override fun getRefreshKey(state: PagingState<Int, DuanziListModel.Datum>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DuanziListModel.Datum> {
        return try {
            val page = params.key ?: 1
            val response = service.invoke()
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = page + 1
            val decryptData = response.data.filter {
                it.joke.videoURL.isBase64Format() && it.joke.imageURL.isBase64Format() && it.joke.thumbURL.isBase64Format()
            }
            LoadResult.Page(decryptData, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun String.isBase64Format(): Boolean {
        return try {
            if (isEmpty()) return true
            substringAfter("ftp://").decodeToByteArrayByBase64(Base64.NO_WRAP)
            true
        } catch (e: Exception) {
            false
        }
    }
}