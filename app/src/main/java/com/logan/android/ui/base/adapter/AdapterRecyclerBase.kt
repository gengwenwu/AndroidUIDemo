package com.logan.android.ui.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.*

/**
 * desc: Recycler view adapter <br/>
 * time: 2020/6/5 6:07 PM <br/>
 * author: Logan <br/>
 * since V 1.0 <br/>
 */
abstract class AdapterRecyclerBase<VH : ViewHolder?, T>(
    context: Context, list: MutableList<T>?
) : RecyclerView.Adapter<VH?>() {

    private var mContext: Context = context
    private var dataList: MutableList<T>? = list
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private var mOnRecyclerItemClickListener: OnRecyclerItemClickListener? = null
    private var mOnRecyclerItemLongClickListener: OnRecyclerItemLongClickListener? = null


    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        if (holder is BaseViewHolder) {
            (holder as? BaseViewHolder)?.let {
                if (!it.hasClick() && mOnRecyclerItemClickListener != null) {
                    it.setOnRecyclerItemClickListener(mOnRecyclerItemClickListener)
                }

                if (!it.hasLongClick() && mOnRecyclerItemLongClickListener != null) {
                    it.setOnRecyclerItemLongClickListener(mOnRecyclerItemLongClickListener)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (dataList == null) {
            return 0
        } else {
            return dataList!!.size
        }
    }

    fun getLayoutInflater() = mLayoutInflater

    fun getContext() = mContext

    fun getList(): List<T> {
        return (if (dataList == null) ArrayList() else dataList)!!
    }

    fun setList(list: MutableList<T>?) {
        dataList = list
    }

    fun addAll(list: List<T>?) {
        list?.let {
            if (dataList != null) {
                dataList!!.addAll(it)
            }
        }
    }

    fun addAll(index: Int, list: List<T>?) {
        if (dataList != null) {
            dataList!!.addAll(index, list!!)
        }
    }

    fun clear() {
        if (dataList != null) {
            dataList!!.clear()
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnRecyclerItemClickListener?) {
        if (onItemClickListener != null) {
            mOnRecyclerItemClickListener = onItemClickListener
        }
    }

    fun setOnItemLongClickLitener(onItemLongClickListener: OnRecyclerItemLongClickListener?) {
        if (onItemLongClickListener != null) {
            mOnRecyclerItemLongClickListener = onItemLongClickListener
        }
    }

    interface OnRecyclerItemClickListener {
        fun onItemClick(v: View?, position: Int)
    }

    interface OnRecyclerItemLongClickListener {
        fun onItemLongClick(v: View?, position: Int)
    }

}