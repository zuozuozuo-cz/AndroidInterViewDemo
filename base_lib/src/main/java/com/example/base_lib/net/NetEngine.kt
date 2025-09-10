package com.example.base_lib.net

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

class NetEngine private constructor() {
    private val retrofit: Retrofit

    @Volatile
    private var baseUrl: String = NetConstant.URL_ZHIHU_BASE

    companion object {
        private object Holder {
            val INSTANCE = NetEngine()
        }

        fun getInstance(): NetEngine {
            return Holder.INSTANCE
        }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getUnsafeClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 动态设置baseUrl
     */
    fun setBaseUrl(url: String) {
        if (url.isNotBlank() && url != baseUrl) {
            baseUrl = url
        }
    }

    private fun getUnsafeClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // 创建信任所有证书的TrustManger
        val trustAllCerts = arrayOf<X509TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // 信任所有客户端证书
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // 信任所有服务器证书
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }

        })

        // 配置SSL上下文
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val socketFactory = sslContext.socketFactory

            builder.sslSocketFactory(socketFactory, trustAllCerts[0])
            builder.hostnameVerifier { _, _ -> true }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        // 添加网络日志拦截器
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(httpLoggingInterceptor)

        builder.addInterceptor { chain ->
            // 获取原始请求
            val request = chain.request()
            // 获取原始请求URL
            val oldUrl = request.url
            val newBaseUrl =
                baseUrl.toHttpUrlOrNull()
                    ?: throw IllegalArgumentException("Invalid baseUrl:$baseUrl")
            val newUrl = oldUrl.newBuilder()
                .scheme(newBaseUrl.scheme)
                .host(newBaseUrl.host)
                .port(newBaseUrl.port)
                .encodedPath(newBaseUrl.encodedPath + oldUrl.encodedPath.substringAfterLast("/"))
                .build()

            // 构建新的请求，将URL 替换成 newURL
            val newRequest = request.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }

        // 配置超时设置
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.retryOnConnectionFailure(true)//允许连接失败重试
        return builder.build()
    }

    /**
     * 创建API服务实例
     * @param serviceClass API服务接口类
     * @return API服务实例
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}