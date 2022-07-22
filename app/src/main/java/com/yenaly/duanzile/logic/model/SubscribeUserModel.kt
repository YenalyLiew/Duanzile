package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 21:25
 */
data class SubscribeUserModel(
    override val code: Int,
    override val msg: String,
    val data: List<Datum>
) : IDuanzileModel {
    data class Datum(
        val attention: Long,
        val avatar: String,
        val nickname: String,
        val signature: String,

        @SerializedName(value = "userId")
        val userID: Long
    )
}