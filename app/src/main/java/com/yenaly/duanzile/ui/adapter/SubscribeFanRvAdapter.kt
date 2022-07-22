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
import com.yenaly.duanzile.databinding.ItemLikeUserBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.logic.model.SubscribeUserModel
import com.yenaly.duanzile.ui.activity.UserActivity
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.startActivity

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/22 022 22:00
 */
class SubscribeFanRvAdapter :
    PagingDataAdapter<SubscribeUserModel.Datum, SubscribeFanRvAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SubscribeUserModel.Datum>() {
            override fun areItemsTheSame(
                oldItem: SubscribeUserModel.Datum,
                newItem: SubscribeUserModel.Datum
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: SubscribeUserModel.Datum,
                newItem: SubscribeUserModel.Datum
            ): Boolean {
                return oldItem.userID == newItem.userID
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
        val binding = ItemLikeUserBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.name.text = item.nickname
            Glide.with(context).load(item.avatar.ftpDecrypt()).circleCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.binding.avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_like_user, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val item = getItem(position)
            item?.let {
                context.activity?.startActivity<UserActivity>(TO_USER_ACTIVITY_ID to item.userID)
            }
        }
        return viewHolder
    }
}