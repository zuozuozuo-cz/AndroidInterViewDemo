package com.example.base_lib.net

enum class Status {
    LOADING, // 加载中
    MORE_ADD,//加载更多成功
    SUCCEED,//成功
    ERROR// 失败
}

/**
 * 一个通用的资源包装类，用来表示数据加载的不同状态（加载中/成功/失败等）
 * 泛型 T 表示具体的数据类型
 */
data class Resource<T>(
    val status: Status, // 当前资源的状态
    val data: T? = null,  // 资源对应的数据，可以为空
    val message: String? = null// 附带的消息（错误信息/提示信息）
) {
    companion object {
        /**
         * 成功状态（带数据）
         */
        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCEED, data, null)

        /**
         * 成功状态（带数据和消息）
         */
        fun <T> success(data: T, msg: String): Resource<T> = Resource(Status.SUCCEED, data, msg)

        /**
         * 错误状态（可带错误消息和已有数据）
         */
        fun <T> error(msg: String, data: T? = null): Resource<T> = Resource(Status.ERROR, data, msg)

        /**
         * 加载中状态（可带已有数据）
         */
        fun <T> loading(data: T? = null): Resource<T> = Resource(Status.LOADING, data, null)

        /**
         * 加载中状态（带消息）
         */
        fun <T> loading(data: T? = null, msg: String): Resource<T> =
            Resource(Status.LOADING, data, msg)


        /**
         * 加载更多成功状态（可带已有数据）
         */
        fun <T> moreSucceed(data: T? = null): Resource<T> = Resource(Status.MORE_ADD, data, null)

        /**
         * 加载更多成功状态（带数据和消息）
         */
        fun <T> moreSucceed(data: T? = null, msg: String): Resource<T> =
            Resource(Status.MORE_ADD, data, msg)

    }
}