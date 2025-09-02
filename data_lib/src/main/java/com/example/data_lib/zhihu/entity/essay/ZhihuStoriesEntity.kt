package com.example.data_lib.zhihu.entity.essay

import com.example.data_lib.zhihu.model.IEssayItem
import com.google.gson.annotations.SerializedName

//class ZhihuStoriesEntity : IEssayItem {
//
//    var images: Array<String>? = null
//    var type: Int = 0
//    var id: Long = 0
//    var ga_grefix: String? = null
//    var title: String = ""
//    var zhihuItemId: Int = 0
//
//    private var date: String = ""
//
//    /**
//     * 实现接口：获取封面图URL
//     * @return 封面图URL，如果有多个则返回第一个
//     */
//    override fun getImageUrl(): String {
//        // 安全访问数组：如果images非空且有元素，返回第一个URL，否则返回空字符串
//        return images?.firstOrNull() ?: ""
//    }
//    /**
//     * 实现接口：获取文章标题
//     * @return 文章标题
//     */
//    override fun getTitle(): String = title
//    /**
//     * 实现接口：获取发布日期
//     * @return 日期字符串
//     */
//    override fun getDate(): String = date
//    /**
//     * 实现接口：获取作者名
//     * @return 作者名字符串
//     */
//    override fun getAuthor(): String = ga_grefix ?: ""
//
//
//}

data class ZhihuStoriesEntity(
    @SerializedName("images")
    val images: List<String>? = null,   // 知乎返回的是数组，用 List<String>

    @SerializedName("type")
    val type: Int = 0,

    @SerializedName("id")
    val id: Long = 0,

    @SerializedName("ga_prefix")  // ⚠️ 原来写成 ga_grefix，看起来是拼写错误
    val gaPrefix: String? = null,

    @SerializedName("title")
    private val rawTitle: String = "",  // ⚠️ 避免和 getTitle() 冲突

    @SerializedName("zhihuItemId")
    val zhihuItemId: Int = 0,

    @SerializedName("date")
    private val rawDate: String = ""    // Kotlin data class 字段最好 immutable
): IEssayItem{
    /**
     * 获取封面图（取第一个）
     */
    override fun getImageUrl(): String = images?.firstOrNull() ?: ""

    /**
     * 获取文章标题
     */
    override fun getTitle(): String = rawTitle

    /**
     * 获取发布日期
     */
    override fun getDate(): String = rawDate

    /**
     * 获取作者名（这里知乎 API 返回的是 ga_prefix）
     */
    override fun getAuthor(): String = gaPrefix ?: ""
}