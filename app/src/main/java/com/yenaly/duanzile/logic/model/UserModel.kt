package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/16 016 17:57
 */
data class UserModel(
    override val code: Int,
    val data: Data,
    override val msg: String
) : DuanzileModel {
    data class Data(
        val info: Info,
        val user: User
    ) {
        data class Info(
            val attentionNum: Long,
            val experienceNum: Long,
            val fansNum: Long,
            val likeNum: Long
        )

        data class User(
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



