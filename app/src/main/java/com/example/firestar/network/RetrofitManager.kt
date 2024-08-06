package com.example.firestar.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    private const val BASE_URL = "http://8.134.79.156:7688"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private fun createOkHttpClient() : OkHttpClient {
        val okBuilder = OkHttpClient.Builder().apply {
            addInterceptor {
                val token = TokenManager.getCurrentToken()
                val original = it.request()
                val request = original.newBuilder()
                    .addHeader("Authorization","Bearer $token")
                    .header("Content-Type", "application/json")
                    .header("connection", "Keep-Alive")
                    .method(original.method, original.body)
                    .build()
                it.proceed(request)
            }
        }
            .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间为 30 秒
            . readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间为 30 秒
        return  okBuilder.build()
    }

    fun <T> create(serviceClass: Class<T>) : T = retrofit.create(serviceClass)
    inline fun <reified T> create() : T = create(T::class.java)


}