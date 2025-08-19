package com.example.data_lib.zhihu.entity

import com.example.data_lib.zhihu.model.IEssayItem

class ZhihuStoriesEntity : IEssayItem {

    var images: Array<String>? = null
    var type: Int = 0
    var id: Long = 0
    var ga_grefix: String? = null
    var title: String = ""
    var zhihuItemId: Int = 0
    // 为什么只有这个字段需要set get？
    // 接口的作用是什么？
    private var date: String = ""

    /**
     * 实现接口：获取封面图URL
     * @return 封面图URL，如果有多个则返回第一个
     */
    override fun getImageUrl(): String {
        // 安全访问数组：如果images非空且有元素，返回第一个URL，否则返回空字符串
        return images?.firstOrNull() ?: ""
    }
    /**
     * 实现接口：获取文章标题
     * @return 文章标题
     */
    override fun getTitle(): String = title
    /**
     * 实现接口：获取发布日期
     * @return 日期字符串
     */
    override fun getDate(): String = date
    /**
     * 实现接口：获取作者名
     * @return 作者名字符串
     */
    override fun getAuthor(): String = ga_grefix ?: ""


}