package com.yenaly.duanzile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yenaly.duanzile.*
import com.yenaly.duanzile.databinding.ItemDuanziBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.duanzile.ui.activity.DuanziActivity
import com.yenaly.duanzile.ui.activity.MainActivity
import com.yenaly.duanzile.ui.activity.UserActivity
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.shareText
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.startActivity
import com.yenaly.yenaly_libs.utils.view.clickTrigger

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 16:06
 */
class DuanziRvAdapter :
    PagingDataAdapter<DuanziListModel.Datum, DuanziRvAdapter.ViewHolder>(COMPARATOR) {

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<DuanziListModel.Datum>() {
            override fun areItemsTheSame(
                oldItem: DuanziListModel.Datum,
                newItem: DuanziListModel.Datum
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DuanziListModel.Datum,
                newItem: DuanziListModel.Datum
            ): Boolean {
                return oldItem.joke.jokesID == newItem.joke.jokesID
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
        val binding = ItemDuanziBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.name.text = item.user.nickName
            holder.binding.motto.text = item.user.signature
            holder.binding.subscribe.isGone = item.info.isAttention
            Glide.with(context).load(item.user.avatar).circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.avatar)

            holder.binding.content.text = item.joke.content
            if (item.joke.imageURL.isEmpty()) {
                holder.binding.image.isGone = true
            } else {
                holder.binding.image.isGone = false
                val contentPicDecryptUrl = item.joke.imageURL.ftpDecrypt()
                Glide.with(context).load(contentPicDecryptUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binding.image)
            }
            if (item.joke.videoURL.isEmpty()) {
                holder.binding.video.isGone = true
            } else {
                holder.binding.video.isGone = false
                val contentVideoThumbnailDecryptUrl = item.joke.thumbURL.ftpDecrypt()
                val contentVideoDecryptUrl = item.joke.videoURL.ftpDecrypt()
                holder.binding.video.setUp(contentVideoDecryptUrl, null)
                Glide.with(context).load(contentVideoThumbnailDecryptUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binding.video.posterImageView)
            }
            holder.binding.btnThumbUp.text = item.info.likeNum.toString()
            holder.binding.btnThumbDown.text = item.info.disLikeNum.toString()
            holder.binding.btnReply.text = item.info.commentNum.toString()
            holder.binding.btnShare.text = item.info.shareNum.toString()
            if (item.info.isLike) {
                holder.binding.btnThumbUp.setIconResource(R.drawable.ic_baseline_thumb_up_alt_24)
            } else {
                holder.binding.btnThumbUp.setIconResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
            }
            if (item.info.isUnlike) {
                holder.binding.btnThumbDown.setIconResource(R.drawable.ic_baseline_thumb_down_alt_24)
            } else {
                holder.binding.btnThumbDown.setIconResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_duanzi, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.binding.video.apply {
            findViewById<View>(cn.jzvd.R.id.layout_top).background = null
            posterImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val item = getItem(position)
            item?.let {
                context.activity?.startActivity<DuanziActivity>(TO_DUANZI_ACTIVITY_ID to item.joke.jokesID)
            }
        }
        viewHolder.binding.btnReply.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val item = getItem(position)
            item?.let {
                context.activity?.startActivity<DuanziActivity>(TO_DUANZI_ACTIVITY_ID to item.joke.jokesID)
            }
        }
        viewHolder.binding.btnShare.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val item = getItem(position)
            item?.let {
                shareText(item.joke.content)
            }
        }
        (context as? MainActivity)?.apply {
            viewHolder.binding.avatar.setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    startActivity<UserActivity>(TO_USER_ACTIVITY_ID to item.user.userID)
                }
            }
            viewHolder.binding.btnThumbUp.clickTrigger(lifecycle) {
                if (!isLogin) {
                    showShortToast("请先登录")
                    return@clickTrigger
                }
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    viewHolder.binding.btnThumbUp.like(
                        item.joke.jokesID.toString(),
                        !item.info.isLike,
                        likeAction = {
                            item.info.likeNum += 1
                            item.info.isLike = true
                            setIconResource(R.drawable.ic_baseline_thumb_up_alt_24)
                            text = text.toString().toLong().plus(1).toString()
                        },
                        cancelLikeAction = {
                            item.info.likeNum -= 1
                            item.info.isLike = false
                            setIconResource(R.drawable.ic_baseline_thumb_up_off_alt_24)
                            text = text.toString().toLong().minus(1).toString()
                        }
                    )
                }
            }
            viewHolder.binding.btnThumbDown.clickTrigger(lifecycle) {
                if (!isLogin) {
                    showShortToast("请先登录")
                    return@clickTrigger
                }
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    viewHolder.binding.btnThumbDown.unlike(
                        item.joke.jokesID.toString(),
                        !item.info.isUnlike,
                        unlikeAction = {
                            item.info.disLikeNum += 1
                            item.info.isUnlike = true
                            setIconResource(R.drawable.ic_baseline_thumb_down_alt_24)
                            text = text.toString().toLong().plus(1).toString()
                        },
                        cancelUnlikeAction = {
                            item.info.disLikeNum -= 1
                            item.info.isUnlike = false
                            setIconResource(R.drawable.ic_baseline_thumb_down_off_alt_24)
                            text = text.toString().toLong().minus(1).toString()
                        }
                    )
                }
            }
            viewHolder.binding.subscribe.clickTrigger(lifecycle) {
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    viewHolder.binding.subscribe.subscribe(
                        item.user.userID.toString(),
                        !item.info.isAttention,
                        subscribeAction = {
                            viewHolder.binding.subscribe.isGone = true
                            item.info.isAttention = true
                        },
                        cancelSubscribeAction = {
                            viewHolder.binding.subscribe.isGone = false
                            MaterialAlertDialogBuilder(context)
                                .setTitle("确定取消关注吗")
                                .setPositiveButton("确定") { _, _ ->
                                    setIconResource(R.drawable.ic_baseline_add_24)
                                    text = "关注"
                                    item.info.isAttention = false
                                    showShortToast("取关成功")
                                }
                                .setNegativeButton("取消", null)
                                .show()
                        }
                    )
                }
            }
        }

        return viewHolder
    }
}