package com.yenaly.duanzile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yenaly.duanzile.R
import com.yenaly.duanzile.databinding.ItemPicViewBinding
import com.yenaly.yenaly_libs.utils.SystemStatusUtil
import com.yenaly.yenaly_libs.utils.activity
import com.yenaly.yenaly_libs.utils.setSystemBarIconLightMode

/**
 * @project Duanzile
 * @author Yenaly Liew
 * @time 2022/07/25 025 18:32
 */
class PicViewVpAdapter(private val data: List<String>) :
    RecyclerView.Adapter<PicViewVpAdapter.ViewHolder>() {

    private var recyclerView: RecyclerView? = null

    private val context: Context
        get() {
            checkNotNull(recyclerView) {
                "You have not attached adapter to RecyclerView!"
            }
            return recyclerView!!.context
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemPicViewBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pic_view, parent, false)
        val viewHolder = ViewHolder(view)
        val activity = context.activity ?: throw NullPointerException("???")
        val fullToolbar: View = activity.findViewById(R.id.pva_full_toolbar)
        viewHolder.binding.photoView.setOnPhotoTapListener { _, _, _ ->
            fullToolbar.isInvisible = fullToolbar.isVisible
            SystemStatusUtil.fullScreen(activity.window, true, true)
            activity.window.setSystemBarIconLightMode(fullToolbar.isInvisible)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(data[position]).into(holder.binding.photoView)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}