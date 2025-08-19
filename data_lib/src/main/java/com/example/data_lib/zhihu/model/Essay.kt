package com.example.data_lib.zhihu.model

/**
 * 定义顶层文章实体接口
 */
interface Essay {
    /**
     * 获取文章唯一标识ID
     * @return 文章ID
     */
    val id: Int

    /**
     * 获取文章作者
     * @return 作者名称
     */
    val author: String

    /**
     * 获取文章标题
     * @return 文章标题
     */
    val title: String

    /**
     * 获取文章摘要
     * @return 文章摘要
     */
    val digest: String

    /**
     * 获取文章完整内容
     * @return 文章内容
     */
    val content: String

    /**
     * 获取文章字数统计
     * @return 字数统计
     */
    val wc: Long
}