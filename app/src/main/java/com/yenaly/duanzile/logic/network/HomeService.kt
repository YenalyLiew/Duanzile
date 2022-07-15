package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.DuanziListModel
import retrofit2.http.POST

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 16:18
 */
interface HomeService : DuanzileService {
    @POST("home/recommend")
    suspend fun getRecommend(): DuanziListModel

    @POST("home/latest")
    suspend fun getLatest(): DuanziListModel

    @POST("home/pic")
    suspend fun getAllPic(): DuanziListModel

    @POST("home/text")
    suspend fun getAllText(): DuanziListModel
}