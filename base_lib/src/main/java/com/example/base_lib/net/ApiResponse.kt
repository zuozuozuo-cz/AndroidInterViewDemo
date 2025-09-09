package com.example.base_lib.net

import retrofit2.Response


/**
 * API 相应密封类
 * 封装网络请求的结果状态
 */
sealed class ApiResponse<T> {
    data class Success<T>(val body: T) : ApiResponse<T>()
    data class Error<T>(val errorMessage: String, val body: T?) : ApiResponse<T>()

    companion object {
        /**
         * 从Retrofit Response创建 ApiResponse
         */
        fun <T> create(response: Response<T>): ApiResponse<T> {
            return (if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Error("Empty response", null)
                } else {
                    Success(body)
                }
            } else {
                val msg = response.errorBody()?.string() ?: response.message()
                Error(msg, null)
            }) as ApiResponse<T>
        }

        fun <T> error(errorMessage: String, body: T?): ApiResponse<T> {
            return Error(errorMessage, body)
        }
    }
}
