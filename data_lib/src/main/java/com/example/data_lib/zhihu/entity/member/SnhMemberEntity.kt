package com.example.data_lib.zhihu.entity.member

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "member")
data class SnhMemberEntity(

    /** 本地数据库主键，自增 */
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0,

    /** 成员 ID */
    @SerializedName("sid")
    @PrimaryKey(autoGenerate = false)
    val sid: String,

    /** 成员姓名（中文名） */
    @SerializedName("sname")
    val sname: String? = null,

    /** 昵称 */
    @SerializedName("nickname")
    val nickname: String? = null,

    /** 生日，格式如 "06.29" */
    @SerializedName("birth_day")
    val birthDay: String? = null,

    /** 身高，单位 cm */
    @SerializedName("height")
    val height: String? = null,

    /** 所属公司名称 */
    @SerializedName("company")
    val company: String? = null,

    /** 入团日期 */
    @SerializedName("join_day")
    val joinDay: String? = null,

    /** 十二星座 */
    @SerializedName("star_sign_12")
    val starSign12: String? = null,

    /** 出生地 */
    @SerializedName("birth_place")
    val birthPlace: String? = null
) {
    fun getImageUrl(): String {
        return "https://www.snh48.com/images/member/zp_${sid}.jpg"
    }
}