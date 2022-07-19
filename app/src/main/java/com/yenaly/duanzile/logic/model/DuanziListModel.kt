package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 15:11
 */
data class DuanziListModel(
    override val code: Int,
    val data: List<Datum>,
    override val msg: String
) : IDuanzileModel {
    data class Datum(
        val info: Info,
        val joke: Joke,
        val user: User,

        @SerializedName("cover")
        val forPersonalVideoCover: String?,

        @SerializedName("jokeId")
        val forPersonalVideoJokeID: Long,

        @SerializedName("likeNum")
        val forPersonalVideoLikeNum: String?
    ) {
        data class Info(
            val commentNum: Long,
            var disLikeNum: Long,
            var isAttention: Boolean,
            var isLike: Boolean,
            var isUnlike: Boolean,
            var likeNum: Long,
            var shareNum: Long
        )

        data class Joke(
            val addTime: String,

            @SerializedName(value = "audit_msg")
            val auditMsg: String,

            val content: String,
            val hot: Boolean,
            val imageSize: String,

            @SerializedName(value = "imageUrl")
            val imageURL: String,

            @SerializedName(value = "jokesId")
            val jokesID: Long,

            val latitudeLongitude: String,
            val showAddress: String,

            @SerializedName(value = "thumbUrl")
            val thumbURL: String,

            val type: Int,

            @SerializedName(value = "userId")
            val userID: Long,

            val videoSize: String,
            val videoTime: Long,

            @SerializedName(value = "videoUrl")
            val videoURL: String
        )

        data class User(
            val avatar: String,
            val nickName: String,
            val signature: String,

            @SerializedName(value = "userId")
            val userID: Long
        )
    }
}
