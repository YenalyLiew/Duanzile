package com.yenaly.duanzile.logic.model

import com.google.gson.annotations.SerializedName

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 14:30
 */
data class CommentModel(
    override val code: Int,
    override val msg: String,
    val data: Data
) : IDuanzileModel {
    data class Data(
        val comments: List<Comment>,
        val count: Long
    ) {
        data class Comment(
            @SerializedName(value = "commentId")
            val commentID: Long,

            val commentUser: CommentUser,
            val content: String,
            var isLike: Boolean,
            val itemCommentList: List<ItemCommentList>,
            val itemCommentNum: Long,

            @SerializedName(value = "jokeId")
            val jokeID: Long,

            @SerializedName(value = "jokeOwnerUserId")
            val jokeOwnerUserID: Long,

            var likeNum: Long,
            val timeStr: String
        ) {
            data class CommentUser(
                val nickname: String,
                val userAvatar: String,

                @SerializedName(value = "userId")
                val userID: Long
            )

            data class ItemCommentList(
                @SerializedName(value = "commentItemId")
                val commentItemID: Long,

                @SerializedName(value = "commentParentId")
                val commentParentID: Long,

                val commentUser: CommentUser,
                val commentedNickname: String,

                @SerializedName(value = "commentedUserId")
                val commentedUserID: Long,

                val content: String,
                val isReplyChild: Boolean,

                @SerializedName(value = "jokeId")
                val jokeID: Long,

                val timeStr: String
            )
        }
    }
}
