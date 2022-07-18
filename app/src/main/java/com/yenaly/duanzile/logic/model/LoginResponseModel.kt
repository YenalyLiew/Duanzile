package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 12:01
 */
data class LoginResponseModel(
    override val code: Int,
    val data: Data,
    override val msg: String
) : IDuanzileModel {
    data class Data(
        val token: String,
        val type: String,
        val userInfo: UserInfo
    ) {
        data class UserInfo(
            val avatar: String,
            val birthday: String,
            val inviteCode: String,
            val invitedCode: String,
            val nickname: String,
            val sex: String,
            val signature: String,

            @SerializedName(value = "userId")
            val userID: Long,

            val userPhone: String
        )
    }
}