package com.example.data_lib.member.api

import com.example.data_lib.member.entity.MemberResponse
import com.example.data_lib.member.entity.SnhMemberEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * * 请求地址：
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=10  # SNH48 上海正团
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=20  # BEJ48 北京姐妹团
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=30  # GNZ48 广州姐妹团
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=40  # CKG48 重庆姐妹团
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=50  # CGT48 成都姐妹团（已成立）
 *  * https://h5.48.cn/resource/jsonp/allmembers.php?gid=60  # JNR48 少儿练习生团计划
 */
interface SnhMemberWebService {

    @GET("allmembers.php?")
    fun getMemberList(@Query("gid") gid: Int): Call<MemberResponse>

    @GET("")
    fun getMemberBySid(sid: String): SnhMemberEntity
}