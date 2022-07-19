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
) : IDuanzileModel {
    data class Data(
        val attentionNum: String,
        var attentionState: Int,
        val avatar: String,
        val collectNum: String,
        val commentNum: String,
        val cover: String,
        val fansNum: String,
        val joinTime: String,
        val jokeLikeNum: String,
        val jokesNum: String,
        val likeNum: String,
        val nickname: String,

        @SerializedName("sigbature")
        val signature: String,

        @SerializedName(value = "userId")
        val userID: Long
    )
}



