package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.*
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 17:37
 */
interface UserService : DuanzileService {
    @POST("user/info")
    suspend fun getLoginUserInfo(): LoginUserModel

    @POST("user/info/target")
    suspend fun getUserInfo(
        @Query("targetUserId") id: String
    ): UserModel

    @POST("jokes/whole_jokes/list")
    suspend fun getUserDuanzi(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): DuanziListModel

    @POST("jokes/video/list")
    suspend fun getUserDuanziVideo(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): DuanziListModel

    @POST("jokes/whole_jokes/like/list")
    suspend fun getUserLikedDuanzi(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): DuanziListModel

    @POST("jokes/video/like/list")
    suspend fun getUserLikedDuanziVideo(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): DuanziListModel


    @POST("user/attention/list")
    suspend fun getUserSubscribeList(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): SubscribeUserModel

    @POST("user/fans/list")
    suspend fun getUserFanList(
        @Query("targetUserId") id: String,
        @Query("page") page: Int
    ): SubscribeUserModel
}