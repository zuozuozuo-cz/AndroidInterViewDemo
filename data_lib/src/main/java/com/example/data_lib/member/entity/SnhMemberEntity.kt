package com.example.data_lib.member.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "member")
//data class SnhMemberEntity(
//
//    /** 本地数据库主键，自增 */
////    @PrimaryKey(autoGenerate = true)
////    val id: Long = 0,
//
//    /** 成员 ID */
//    @SerializedName("sid")
//    @PrimaryKey(autoGenerate = false)
//    val sid: String,
//
//    /** 成员姓名（中文名） */
//    @SerializedName("sname")
//    val sname: String? = null,
//
//    /** 昵称 */
//    @SerializedName("nickname")
//    val nickname: String? = null,
//
//    /** 生日，格式如 "06.29" */
//    @SerializedName("birth_day")
//    val birthDay: String? = null,
//
//    /** 身高，单位 cm */
//    @SerializedName("height")
//    val height: String? = null,
//
//    /** 所属公司名称 */
//    @SerializedName("company")
//    val company: String? = null,
//
//    /** 入团日期 */
//    @SerializedName("join_day")
//    val joinDay: String? = null,
//
//    /** 十二星座 */
//    @SerializedName("star_sign_12")
//    val starSign12: String? = null,
//
//    /** 出生地 */
//    @SerializedName("birth_place")
//    val birthPlace: String? = null
//) {
//    fun getImageUrl(): String {
//        return "https://www.snh48.com/images/member/zp_${sid}.jpg"
//    }
//}
@Entity(tableName = "member")
data class SnhMemberEntity(

    /** 成员 ID */
    @SerializedName("sid")
    @PrimaryKey(autoGenerate = false)
    val sid: String,

    /** 队伍 ID */
    @SerializedName("gid")
    val gid: String? = null,

    /** 队伍名称（如 SNH、BEJ） */
    @SerializedName("gname")
    val gname: String? = null,

    /** 成员姓名（中文名） */
    @SerializedName("sname")
    val sname: String? = null,

    /** 成员姓名（带空格的全名） */
    @SerializedName("fname")
    val fname: String? = null,

    /** 拼音 */
    @SerializedName("pinyin")
    val pinyin: String? = null,

    /** 缩写 */
    @SerializedName("abbr")
    val abbr: String? = null,

    /** 团体 ID */
    @SerializedName("tid")
    val tid: String? = null,

    /** 团体名称 */
    @SerializedName("tname")
    val tname: String? = null,

    /** 分组 ID */
    @SerializedName("pid")
    val pid: String? = null,

    /** 分组名称 */
    @SerializedName("pname")
    val pname: String? = null,

    /** 昵称 */
    @SerializedName("nickname")
    val nickname: String? = null,

    /** 所属公司名称 */
    @SerializedName("company")
    val company: String? = null,

    /** 入团日期 */
    @SerializedName("join_day")
    val joinDay: String? = null,

    /** 身高，单位 cm */
    @SerializedName("height")
    val height: String? = null,

    /** 生日，格式如 "06.29" */
    @SerializedName("birth_day")
    val birthDay: String? = null,

    /** 十二星座 */
    @SerializedName("star_sign_12")
    val starSign12: String? = null,

    /** 48星座 */
    @SerializedName("star_sign_48")
    val starSign48: String? = null,

    /** 出生地 */
    @SerializedName("birth_place")
    val birthPlace: String? = null,

    /** 特长 */
    @SerializedName("speciality")
    val speciality: String? = null,

    /** 爱好 */
    @SerializedName("hobby")
    val hobby: String? = null,

    /** 经历 */
    @SerializedName("experience")
    val experience: String? = null,

    /** 口号/自我介绍 */
    @SerializedName("catch_phrase")
    val catchPhrase: String? = null,

    /** 微博 UID */
    @SerializedName("weibo_uid")
    val weiboUid: String? = null,

    /** 微博认证信息 */
    @SerializedName("weibo_verifier")
    val weiboVerifier: String? = null,

    /** 血型 */
    @SerializedName("blood_type")
    val bloodType: String? = null,

    /** 百度贴吧关键字 */
    @SerializedName("tieba_kw")
    val tiebaKw: String? = null,

    /** 成员状态 */
    @SerializedName("status")
    val status: String? = null,

    /** 排名 */
    @SerializedName("ranking")
    val ranking: String? = null,

    /** 口袋48 ID */
    @SerializedName("pocket_id")
    val pocketId: String? = null,

    /** 是否为新成员 */
    @SerializedName("is_group_new")
    val isGroupNew: String? = null,

    /** 主题色 */
    @SerializedName("tcolor")
    val tcolor: String? = null,

    /** 队伍色 */
    @SerializedName("gcolor")
    val gcolor: String? = null
) {
    fun getImageUrl(): String {
        return "https://www.snh48.com/images/member/zp_${sid}.jpg"
    }
}
