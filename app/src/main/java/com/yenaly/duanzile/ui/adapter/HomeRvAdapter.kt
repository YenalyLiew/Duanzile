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
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.yenaly.duanzile.DECRYPT_KEY
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.ItemDuanziBinding
import com.yenaly.duanzile.logic.model.DuanziListModel
import com.yenaly.yenaly_libs.utils.aesDecrypt
import com.yenaly.yenaly_libs.utils.decodeToByteArrayByBase64

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/14 014 16:06
 */
class HomeRvAdapter :
    PagingDataAdapter<DuanziListModel.Datum, HomeRvAdapter.ViewHolder>(COMPARATOR) {

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
                val contentPicDecryptUrl =
                    String(
                        item.joke.imageURL.substringAfter("ftp://")
                            .decodeToByteArrayByBase64(Base64.NO_WRAP)
                            .aesDecrypt(
                                DECRYPT_KEY.toByteArray(),
                                algorithm = "AES/ECB/PKCS5Padding"
                            )
                    )
                Glide.with(context).load(contentPicDecryptUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.binding.image)
            }
            if (item.joke.videoURL.isEmpty()) {
                holder.binding.video.isGone = true
            } else {
                holder.binding.video.isGone = false
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_duanzi, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.binding.video.apply {
            findViewById<View>(cn.jzvd.R.id.layout_top).background = null
            posterImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return viewHolder
    }
}