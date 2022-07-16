package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.UserModel
import retrofit2.http.POST

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 17:37
 */
interface UserService : DuanzileService {
    @POST("user/info")
    suspend fun getLoginUserInfo(): UserModel
}