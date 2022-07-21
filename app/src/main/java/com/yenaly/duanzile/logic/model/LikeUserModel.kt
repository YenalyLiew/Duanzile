package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 14:51
 */
data class LikeUserModel(
    override val code: Int,
    override val msg: String,
    val data: List<Data>
) : IDuanzileModel {
    data class Data(
        val avatar: String,
        val nickname: String,
        @SerializedName("user_id")
        val userID: Long
    )
}
