package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.GeneralModel
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 14:46
 */
interface GeneralService : DuanzileService {
    @POST("jokes/like")
    suspend fun like(
        @Query("id") id: String,
        @Query("status") status: String
    ): GeneralModel

    @POST("jokes/unlike")
    suspend fun unlike(
        @Query("id") id: String,
        @Query("status") status: String
    ): GeneralModel


}