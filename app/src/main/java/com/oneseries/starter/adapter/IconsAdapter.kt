package com.oneseries.starter.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.oneseries.starter.R
import com.oneseries.starter.bean.IconBean


class IconsAdapter(private val context: Context/*,
                   private var dataList: ArrayList<IconsBean>*/): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
/*
    override fun getSectionName(position: Int): String {
        return dataList[position].category
    }
*/
    private var dataList: MutableList<IconBean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_CATE)
            CateHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_categories, parent, false))
        else
            IconHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_icons, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (isCate(position)) ITEM_TYPE_CATE else ITEM_TYPE_ICON
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val icon = dataList[position]
        if (holder is CateHolder) {
            var cateCount = arrayOf("default", "")
            //Log.d("test", icon.getCate())
            if (icon.cate != null) {
                cateCount = icon.cate.split("\\.\\.\\.".toRegex()).toTypedArray()
                //cateCount[1] = "..." + cateCount[1]
            }
            holder.tvCate.text = cateCount[0]
            holder.tvCount.text = cateCount[1]
        }
        else if (holder is IconHolder){
            holder.icon.setImageResource(icon.id)
            //Glide.with(holder.icon).load(icon.id).into(holder.icon)
            //holder.icon.setImageResource(icon.id)
            //val iconName: String? = icon.name
            holder.icon.setOnClickListener{
                clickListener.onClick(holder.icon, icon.id, icon.name)
            }
        }
    }

    private lateinit var clickListener: OnItemClickListener

    interface OnItemClickListener{
        fun onClick(iconView: ImageView, icon: Int, name: String?)
    }

    fun setClickListener(clickListener: OnItemClickListener) {
        this.clickListener = clickListener
    }

    fun isCate(position: Int): Boolean {
        return dataList[position].name == null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(dataList: MutableList<IconBean>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun attach(dataList: List<IconBean>){
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        this.dataList.clear()
        notifyDataSetChanged()
    }

    class CateHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvCate: TextView = itemView.findViewById(R.id.categoryTitle)
        var tvCount: TextView = itemView.findViewById(R.id.categoryCount)
    }

    class IconHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var icon: ImageView = itemView.findViewById(R.id.icons)
    }

    companion object {
        private const val ITEM_TYPE_CATE = 0
        private const val ITEM_TYPE_ICON = 1
    }
}