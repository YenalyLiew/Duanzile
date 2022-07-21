package com.yenaly.duanzile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yenaly.duanzile.R
import com.yenaly.duanzile.TO_USER_ACTIVITY_ID
import com.yenaly.duanzile.databinding.ItemCommentBinding
import com.yenaly.duanzile.isLogin
import com.yenaly.duanzile.logic.model.CommentModel
import com.yenaly.duanzile.ui.activity.DuanziActivity
import com.yenaly.duanzile.ui.activity.UserActivity
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.startActivity
import com.yenaly.yenaly_libs.utils.view.clickTrigger

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/20 020 15:58
 */
class CommentRvAdapter :
    PagingDataAdapter<CommentModel.Data.Comment, CommentRvAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<CommentModel.Data.Comment>() {
            override fun areItemsTheSame(
                oldItem: CommentModel.Data.Comment,
                newItem: CommentModel.Data.Comment
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: CommentModel.Data.Comment,
                newItem: CommentModel.Data.Comment
            ): Boolean {
                return oldItem.commentID == newItem.commentID
            }
        }
    }

    private var recyclerView: RecyclerView? = null

    private val context: Context
        get() {
            checkNotNull(recyclerView) {
                "You have not attached adapter to RecyclerView!"
            }
            return recyclerView!!.context
        }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCommentBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            Glide.with(context).load(item.commentUser.userAvatar).circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.avatar)
            holder.binding.name.text = item.commentUser.nickname
            holder.binding.content.text = item.content
            holder.binding.date.text = item.timeStr
            holder.binding.btnLike.text = item.likeNum.toString()
            if (item.isLike) {
                holder.binding.btnLike.setIconResource(R.drawable.ic_baseline_favorite_24)
            } else {
                holder.binding.btnLike.setIconResource(R.drawable.ic_baseline_favorite_border_24)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        val viewHolder = ViewHolder(view)
        (context.activity as? DuanziActivity)?.apply {
            viewHolder.binding.avatar.setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    startActivity<UserActivity>(TO_USER_ACTIVITY_ID to item.commentUser.userID)
                }
            }
            viewHolder.binding.btnLike.clickTrigger(lifecycle) {
                if (!isLogin) {
                    showShortToast("请先登录")
                    return@clickTrigger
                }
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    viewHolder.binding.btnLike.commentLike(
                        item.commentID.toString(),
                        !item.isLike,
                        likeAction = {
                            item.likeNum += 1
                            item.isLike = true
                            setIconResource(R.drawable.ic_baseline_favorite_24)
                            text = text.toString().toLong().plus(1).toString()
                        },
                        cancelLikeAction = {
                            item.likeNum -= 1
                            item.isLike = false
                            setIconResource(R.drawable.ic_baseline_favorite_border_24)
                            text = text.toString().toLong().minus(1).toString()
                        }
                    )
                }
            }
        }
        return viewHolder
    }
}