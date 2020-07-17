package com.logan.android.ui.layout.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.logan.android.ui.R
import com.logan.android.ui.layout.recyclerview.adapter.MyRVAdapter2.MyTVHolder

/**
 * desc: rv 适配器 <br/>
 * time: 2020/7/17 11:34 AM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 *   // TODO: 2020/7/17 抽取基类
 */
class MyRVAdapter2(context: Context) : RecyclerView.Adapter<MyTVHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    private var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var data: ArrayList<String> = ArrayList(300)

    init {
        // TODO: 2020/7/17 写死
        for (i in 1..300) {
            data.add("hello $i")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTVHolder {
        return MyTVHolder(layoutInflater.inflate(R.layout.item_rv_text, parent, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyTVHolder, position: Int) {
        holder.tvText?.text = data[position]

        onItemClickListener?.let { itemClickListener ->
            with(holder.itemView) {
                setOnClickListener { // 点击监听
                    itemClickListener.onItemClick(holder.itemView, holder.layoutPosition)
                }

                setOnLongClickListener { // 长按监听
                    itemClickListener.onItemLongClick(holder.itemView, holder.layoutPosition)
                    return@setOnLongClickListener false
                }
            }
        }
    }

    //
    inner class MyTVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // TODO: 2020/7/17 省略这里？
        var tvText: TextView? = null

        init {
            tvText = itemView.findViewById(R.id.tv_text);
        }
    }


    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {

        fun onItemClick(view: View, position: Int)

        fun onItemLongClick(view: View, position: Int)
    }

}