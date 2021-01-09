package com.liwei.kotlin.snapshot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liwei.kotlin.R


/**
 * # ***********************************************************************************************
 * # ClassName:      ShareMenuAdapter$
 * # Function:       分享菜单适配器
 * # @author         ybxie$
 * # @version        Ver 1.0
 * # ***********************************************************************************************
 * # Modified By     ybxie$     2020/3/12$    16:02$
 * # Modifications:  initial
 * # ***********************************************************************************************
 */
class ShareMenuAdapter(val context: Context) : RecyclerView.Adapter<ShareMenuAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    private val mInflater by lazy { LayoutInflater.from(context) }

    private val mData = mutableListOf<ShareModel>()

    private var mClickListener: OnItemClickListener? = null

    fun setData(data: MutableList<ShareModel>?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
    }

    fun getItem(position: Int): ShareModel? {
        return mData.elementAtOrNull(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }



    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.adf_view_share_item, parent, false)
        view.setOnClickListener {
            val tag = it.tag
            if (tag is Int) {
                mClickListener?.onItemClick(it, tag)
            }
        }
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            viewHolder.shareIcon.setImageResource(it.imageResId)
            viewHolder.shareName.text = it.name
            viewHolder.shareItem.tag = position
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mClickListener = listener
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val shareItem = view
        val shareIcon: ImageView =view.findViewById(R.id.adf_share_iv)
        val shareName: TextView =view.findViewById(R.id.adf_share_tv)
    }
}