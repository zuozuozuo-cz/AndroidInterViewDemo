package com.example.data_lib.zhihu.converter

import android.util.Log
import androidx.room.TypeConverter
import com.example.data_lib.zhihu.entity.ZhihuStoriesEntity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException
import java.sql.Date

class DateConverter {


    /**
     * 使用lazy 委托实现延迟初始化：
     * 1、线程安全：默认情况下，lazy是同步的，确保多线程环境下只初始化一次
     * 2、按需创建：首次访问gson 属性的时候才会执行创建
     * 3、性能优化：避免在类加载的时候就创建Gson实例
     * 4、单利
     */
    companion object {
        private val gson: Gson by lazy {
            GsonBuilder().create()
        }

        /**
         * 将Date对象转换为时间戳
         * @param date 日期对象
         * @return 对应的时间戳（毫秒），如果date为null则返回null
         */
        @TypeConverter
        @JvmStatic
        fun toDate(timestamp: Long?): Date? {
            return timestamp?.let { Date(it) }
        }

        /**
         * 将JSON字符串转换为ZhihuStoriesEntity对象列表
         * @param jsonString 包含实体数据的JSON数组字符串
         * @return 解析后的实体对象列表，解析失败返回空列表
         */
        @TypeConverter
        @JvmStatic
        fun toZhihuStoriesEntityList(jsonString: String?): List<ZhihuStoriesEntity> {
            if (jsonString.isNullOrBlank()) return emptyList()

            return try {
                val jsonArray = JSONArray(jsonString)
                List(jsonArray.length()) { index ->
                    gson.fromJson(
                        jsonArray.getJSONArray(index).toString(),
                        ZhihuStoriesEntity::class.java
                    )
                }
            } catch (e: JSONException) {
                Log.e("DateConverter", "JSON解析失败", e)
                emptyList()
            } catch (e: Exception) {
                Log.e("DateConverter", "JSON解析失败", e)
                emptyList()
            }
        }

        /**
         * 将ZhihuStoriesEntity对象列表转换为JSON字符串
         * @param list 实体对象列表
         * @return 表示列表的JSON数组字符串，如果列表为空则返回"[]"
         */
        @TypeConverter
        @JvmStatic
        fun fromZhihuStoriesEntityList(list: List<ZhihuStoriesEntity>?): String {
            if (list.isNullOrEmpty()) return "[]"
            return try {
                val jsonArray = JSONArray().apply {
                    list.forEach { entity ->
                        put(gson.toJson(entity))
                    }
                }
                jsonArray.toString()
            } catch (e: Exception) {
                Log.e("DateConverter", "序列化失败", e)
                "[]"
            }
        }
    }

}