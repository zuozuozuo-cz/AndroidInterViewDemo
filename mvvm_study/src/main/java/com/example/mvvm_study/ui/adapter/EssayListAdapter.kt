package com.example.mvvm_study.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.base_lib.constant.Constant
import com.example.data_lib.zhihu.model.IEssayItem
import com.example.mvvm_study.R

class EssayListAdapter(private val mContext: Context, data: MutableList<MultiItem>) :
    BaseMultiItemQuickAdapter<EssayListAdapter.MultiItem, BaseViewHolder>(data) {

    init {
        val layoutId = R.layout.product_item
        Log.e(Constant.COMMON_TAG, "id:$layoutId")
        addItemType(MultiItem.TYPE_BASE, layoutId)
    }


    override fun convert(holder: BaseViewHolder, item: MultiItem) {
        item.update(mContext, holder)
    }


    class MultiItem(var data: IEssayItem, var type: Int) :
        MultiItemEntity {

        companion object {
            const val TYPE_BASE = 1
        }

        fun update(context: Context, holder: BaseViewHolder) {
            when (type) {
                TYPE_BASE -> {
                    // 设置文本信息
                    holder.setText(R.id.item_title, data.getTitle())
                    holder.setText(R.id.item_time, data.getDate())
                    holder.setText(R.id.item_from, data.getAuthor())

                    Glide.with(context)
                        .load(data.getImageUrl())
                        .centerCrop()
//                        .into(object : SimpleTarget<GlideDrawable>() {
//                            override fun onResourceReady(
//                                resource: GlideDrawable,
//                                transition: Transition<in GlideDrawable>?
//                            ) {
//                                holder.setImageResource(R.id.icon_item, resource)
//                            }
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                holder.setImageDrawable(R.id.icon_item, resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                holder.setImageDrawable(R.id.icon_item, placeholder)

                            }

                        })
                }
            }

        }

        override val itemType: Int
            get() = type
    }

}