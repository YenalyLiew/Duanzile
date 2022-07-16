package com.yenaly.duanzile.logic.network

import com.yenaly.duanzile.logic.model.GeneralModel
import com.yenaly.duanzile.logic.model.LoginUserModel
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 11:41
 */
interface LoginService : DuanzileService {
    @POST("user/login/psw")
    suspend fun loginByPassword(
        @Query("phone") phone: String,
        @Query("psw") password: String
    ): LoginUserModel

    @POST("user/login/code")
    suspend fun loginByVerifyCode(
        @Query("phone") phone: String,
        @Query("code") code: String
    ): LoginUserModel

    @POST("user/login/get_code")
    suspend fun getLoginVerifyCode(
        @Query("phone") phone: String
    ): GeneralModel
}