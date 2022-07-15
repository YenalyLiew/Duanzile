package com.yenaly.duanzile.ui.adapter

import android.content.Context
import android.util.Base64
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
import com.yenaly.duanzile.DECRYPT_KEY
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.ItemSlideVideoBinding
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.yenaly_libs.utils.aesDecrypt
import com.yenaly.yenaly_libs.utils.decodeToByteArrayByBase64

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
            val contentVideoThumbnailDecryptUrl =
                String(
                    item.joke.thumbURL.substringAfter("ftp://")
                        .decodeToByteArrayByBase64(Base64.NO_WRAP)
                        .aesDecrypt(
                            DECRYPT_KEY.toByteArray(),
                            algorithm = "AES/ECB/PKCS5Padding"
                        )
                )
            val contentVideoDecryptUrl =
                String(
                    item.joke.videoURL.substringAfter("ftp://")
                        .decodeToByteArrayByBase64()
                        .aesDecrypt(
                            DECRYPT_KEY.toByteArray(),
                            algorithm = "AES/ECB/PKCS5Padding"
                        )
                )
            holder.binding.video.setUp(contentVideoDecryptUrl, null)
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
        viewHolder.binding.video.apply {
            findViewById<View>(cn.jzvd.R.id.layout_top).background = null
            posterImageView.scaleType = ImageView.ScaleType.FIT_CENTER
        }
        return viewHolder
    }
}