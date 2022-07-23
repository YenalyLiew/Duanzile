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
import com.yenaly.duanzile.R
import com.yenaly.duanzile.TO_DUANZI_ACTIVITY_ID
import com.yenaly.duanzile.databinding.ItemDuanziSimplifiedBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.isLogin
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.duanzile.ui.activity.DuanziActivity
import com.yenaly.duanzile.ui.activity.UserActivity
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.shareText
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.startActivity
import com.yenaly.yenaly_libs.utils.view.clickTrigger

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/17 017 16:49
 */
class SimpleDuanziRvAdapter :
    PagingDataAdapter<DuanziListModel.Datum, SimpleDuanziRvAdapter.ViewHolder>(COMPARATOR) {

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
        val binding = ItemDuanziSimplifiedBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_duanzi_simplified, parent, false)
        val viewHolder = ViewHolder(view)
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
        (context as? UserActivity)?.apply {
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
        }

        return viewHolder
    }
}