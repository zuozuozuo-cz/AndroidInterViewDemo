package com.example.data_lib.zhihu.net

import androidx.annotation.StringDef
import com.example.data_lib.zhihu.entity.essay.ZhihuItemEntity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EssayWebService {
    companion object {
        const val DAY = "today"
        const val RANDOM = "random"
    }

    @StringDef(DAY, RANDOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class EssayType

    @GET("article/{type}?dev=1")
    fun getEssay(@EssayType @Path("type") type: String): Call<ZhihuItemEntity>

    @GET("news/{type}")
    fun getZhihuList(@Path("type") type: String): Call<ZhihuItemEntity>

    @GET("users")
    fun getTest(): Call<ResponseBody>
}