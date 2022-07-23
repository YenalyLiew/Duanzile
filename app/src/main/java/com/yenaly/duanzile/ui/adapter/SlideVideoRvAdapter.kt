package com.yenaly.duanzile.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yenaly.duanzile.R
import com.yenaly.duanzile.TO_USER_ACTIVITY_ID
import com.yenaly.duanzile.databinding.ItemSlideVideoBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.isLogin
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.duanzile.ui.activity.MainActivity
import com.yenaly.duanzile.ui.activity.UserActivity
import com.yenaly.duanzile.ui.view.SlideVideoJzvdStd
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.showShortToast
import com.yenaly.yenaly_libs.utils.startActivity
import com.yenaly.yenaly_libs.utils.view.clickTrigger


/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/15 015 16:41
 */
class SlideVideoRvAdapter :
    PagingDataAdapter<DuanziListModel.Datum, SlideVideoRvAdapter.ViewHolder>(COMPARATOR) {

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
        val binding = ItemSlideVideoBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: SlideVideoRvAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            if (item.info.isLike) {
                holder.binding.btnLike.setIconResource(R.drawable.ic_baseline_favorite_24)
            } else {
                holder.binding.btnLike.setIconResource(R.drawable.ic_baseline_favorite_border_24)
            }
            holder.binding.subscribe.isGone = item.info.isAttention
            holder.binding.btnLike.text = item.info.likeNum.toString()
            holder.binding.btnReply.text = item.info.commentNum.toString()
            holder.binding.btnShare.text = item.info.shareNum.toString()
            Glide.with(context).load(item.user.avatar).circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.avatar)

            holder.binding.name.text = "@${item.user.nickName}"
            holder.binding.description.text = item.joke.content
            val contentVideoThumbnailDecryptUrl = item.joke.thumbURL.ftpDecrypt()
            val contentVideoDecryptUrl = item.joke.videoURL.ftpDecrypt()
            val jzDataSource = JZDataSource(contentVideoDecryptUrl, null)
            jzDataSource.looping = true
            holder.binding.video.setUp(jzDataSource, Jzvd.SCREEN_NORMAL)
            Glide.with(context).load(contentVideoThumbnailDecryptUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.video.posterImageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SlideVideoRvAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_slide_video, parent, false)
        val viewHolder = ViewHolder(view)
        (context.activity as? MainActivity)?.apply {
            viewHolder.binding.avatar.setOnClickListener {
                val position = viewHolder.bindingAdapterPosition
                val item = getItem(position)
                item?.let {
                    startActivity<UserActivity>(TO_USER_ACTIVITY_ID to item.user.userID)
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
                    viewHolder.binding.btnLike.like(
                        item.joke.jokesID.toString(),
                        !item.info.isLike,
                        likeAction = {
                            item.info.likeNum += 1
                            item.info.isLike = true
                            setIconResource(R.drawable.ic_baseline_favorite_24)
                            text = text.toString().toLong().plus(1).toString()
                        },
                        cancelLikeAction = {
                            item.info.likeNum -= 1
                            item.info.isLike = false
                            setIconResource(R.drawable.ic_baseline_favorite_border_24)
                            text = text.toString().toLong().minus(1).toString()
                        }
                    )
                }
            }
        }
        return viewHolder
    }

    var oldPosition = -1
    var currentPosition = -1

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val position = holder.bindingAdapterPosition

        if (currentPosition == -1) {
            autoPlayVideo(0)
        }
        oldPosition = currentPosition
        currentPosition = position
        Log.d("onViewAttachedToWindow", "第${position}个")
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        val position = holder.bindingAdapterPosition

        val childCount = recyclerView!!.childCount
        if (childCount != 1) {
            if (oldPosition < currentPosition) {
                recyclerView!!.getChildAt(childCount - 2)
                    .findViewById<SlideVideoJzvdStd>(R.id.video).onStatePause()
            } else {
                recyclerView!!.getChildAt(childCount - 1)
                    .findViewById<SlideVideoJzvdStd>(R.id.video).onStatePause()
            }
        }
        Log.d("_release", "release here $position.")
        autoPlayVideo(currentPosition)

        Log.d("onViewDetachedFromWindow", "第${position}个")
    }

    private fun autoPlayVideo(position: Int) {
        if (recyclerView == null) {
            return
        }
        Log.d("_play_video", "already $position")
        val childCount = recyclerView!!.childCount
        Log.d("rv_size", childCount.toString())
        if (childCount == 1 || oldPosition < currentPosition) {
            recyclerView!!.getChildAt(childCount - 1)
                .findViewById<SlideVideoJzvdStd>(R.id.video).apply {
                    Log.d("video_url", jzDataSource.currentUrl.toString())
                    startVideoAfterPreloading()
                }
        } else {
            recyclerView!!.getChildAt(childCount - 2)
                .findViewById<SlideVideoJzvdStd>(R.id.video).apply {
                    Log.d("video_url", jzDataSource.currentUrl.toString())
                    startVideoAfterPreloading()
                }
        }
    }
}