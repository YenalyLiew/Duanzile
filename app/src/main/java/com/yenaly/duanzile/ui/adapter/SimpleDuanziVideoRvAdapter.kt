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
import com.yenaly.duanzile.databinding.ItemDuanziVideoSimplifiedBinding
import com.yenaly.duanzile.ftpDecrypt
import com.yenaly.duanzile.logic.model.DuanziListModel

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/17 017 17:22
 */
class SimpleDuanziVideoRvAdapter :
    PagingDataAdapter<DuanziListModel.Datum, SimpleDuanziVideoRvAdapter.ViewHolder>(COMPARATOR) {

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
                return oldItem.forPersonalVideoJokeID == newItem.forPersonalVideoJokeID
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
        val binding = ItemDuanziVideoSimplifiedBinding.bind(view)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.binding.likeNum.text = item.forPersonalVideoLikeNum
            Glide.with(context).load(item.forPersonalVideoCover!!.ftpDecrypt())
                .transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
                .into(holder.binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_duanzi_video_simplified, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val item = getItem(position)
            item?.let {
                // todo
            }
        }
        return viewHolder
    }
}