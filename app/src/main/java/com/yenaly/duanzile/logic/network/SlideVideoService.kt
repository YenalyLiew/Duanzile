package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.DuanziListModel
import retrofit2.http.POST

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/15 015 16:46
 */
interface SlideVideoService : DuanzileService {
    @POST("douyin/list")
    suspend fun getSlideVideo(): DuanziListModel
}