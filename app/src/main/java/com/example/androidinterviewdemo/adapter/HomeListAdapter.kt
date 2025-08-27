package com.example.androidinterviewdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.example.androidinterviewdemo.R
import com.example.androidinterviewdemo.entity.HomeItemEntity

class HomeListAdapter(var dataList: ArrayList<HomeItemEntity>, var context: Context) :
    RecyclerView.Adapter<HomeListViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeListViewHolder {
        val inflate = LayoutInflater.from(context).inflate(
            R.layout.item_home,
            parent,
            false
        )
        return HomeListViewHolder(inflate)
    }

    override fun onBindViewHolder(
        holder: HomeListViewHolder,
        position: Int
    ) {
        val itemEntity = dataList[position]
        holder.bindView(itemEntity)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class HomeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemText: TextView

    init {
        itemText = itemView.findViewById(R.id.itemText)
    }

    fun bindView(itemEntity: HomeItemEntity) {
        itemText.text = itemEntity.itemName

        itemView.setOnClickListener {
            ARouter.getInstance()
                .build(itemEntity.itemPath)
                .navigation()
        }
    }
}