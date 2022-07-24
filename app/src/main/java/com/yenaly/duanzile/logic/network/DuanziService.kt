package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.*
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 14:28
 */
interface DuanziService : DuanzileService {
    @POST("jokes/comment/list")
    suspend fun getComment(
        @Query("jokeId") id: String,
        @Query("page") page: Int
    ): CommentModel

    @POST("jokes/comment/like")
    suspend fun commentLike(
        @Query("commentId") id: String,
        @Query("status") status: String
    ): GeneralModel

    @POST("jokes/like/list")
    suspend fun getLikeUser(
        @Query("jokeId") id: String,
        @Query("page") page: Int
    ): LikeUserModel

    @POST("jokes/target")
    suspend fun getDuanzi(
        @Query("jokeId") id: String
    ): DuanziModel

    @POST("home/jokes/search")
    suspend fun searchDuanzi(
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): DuanziListModel
}