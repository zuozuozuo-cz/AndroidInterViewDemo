package com.example.data_lib.zhihu.model

/**
 * 定义顶层的文章结构，后续所有的文章实体类都按照这个规则来：
 * 1、多态支持：这里我们只实现了知乎文章，也可以新增一个微博文章，也按照这个规则来
 * 2、解耦依赖：UI组件只依赖接口，不依赖具体实现类，业务逻辑变更不影响展示层
 * 3、标准化访问：强制所有文章对象都按照这个规则来，确保数据一致
 * 4、容易扩展
 */
interface IEssayItem {
    /**
     * 获取文章封面图URL
     * @return 图片URL字符串
     */
    fun getImageUrl(): String

    /**
     * 获取文章标题
     * @return 文章标题字符串
     */
    fun getTitle(): String

    /**
     * 获取文章发布日期
     * @return 日期字符串
     */
    fun getDate(): String

    /**
     * 获取文章作者
     * @return 作者名字符串
     */
    fun getAuthor(): String
}