package com.example.firestar.network

import android.content.ContentValues
import android.util.Log
import com.example.firestar.data.LoginRequest
import com.example.firestar.data.Page
import com.example.firestar.data.RegisterRequest
import com.example.firestar.data.SendMessageRequest
import com.example.firestar.data.VerifyCodeRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object NetWork {
    private val service = RetrofitManager.create<Service>()

    suspend fun login(loginRequest: LoginRequest) = service.login(loginRequest).await()

    suspend fun register(registerRequest: RegisterRequest) = service.register(registerRequest).await()

    suspend fun sendCode(email: String) = service.sendCode(email).await()

    suspend fun verifyCode(verifyCodeRequest: VerifyCodeRequest) = service.verifyCode(verifyCodeRequest).await()

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest)  = service.sendMessage(sendMessageRequest).await()

    suspend fun getMessage(senderId:Long,receiverId:Long) = service.getMessage(senderId, receiverId).await()

    suspend fun getUsers() = service.getUsers().await()

    suspend fun userPages(page: Page) = service.userPages(page).await()

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