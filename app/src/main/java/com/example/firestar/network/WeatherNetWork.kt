package com.example.firestar.network

import android.content.ContentValues
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object WeatherNetWork {

    private val service = WeatherRetrofitManager.create<WeatherService>()

    suspend fun getRealtimeWeather(key:String,location:String) = service.getRealtimeWeather(key, location).await()

    //给网络请求方法的返回值增加拓展函数
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    Log.d(ContentValues.TAG, "OnResponse: ${response.code()}")
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}