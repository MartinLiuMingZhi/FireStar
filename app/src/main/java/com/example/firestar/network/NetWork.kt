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

    suspend fun logout() = service.logout().await()

    suspend fun getUsers() = service.getUsers().await()

    suspend fun getUserById(userId:Long) = service.getUserById(userId).await()

    suspend fun getUserPage(page: Page) = service.getUserPage(page).await()

    suspend fun getUserByEmail(email:String) = service.getUserByEmail(email).await()

    suspend fun sendCode(email: String) = service.sendCode(email).await()

    suspend fun verifyCode(verifyCodeRequest: VerifyCodeRequest) = service.verifyCode(verifyCodeRequest).await()

    suspend fun sendMessage(sendMessageRequest: SendMessageRequest)  = service.sendMessage(sendMessageRequest).await()

    suspend fun getMessage(senderId:Long,receiverId:Long) = service.getMessage(senderId, receiverId).await()

    suspend fun createPost(userId:Long,content:String) = service.createPost(userId, content).await()

    suspend fun getAllPosts() = service.getAllPosts().await()

    suspend fun deletePost(id:Long) = service.deletePost(id).await()

    suspend fun likePost(postId:Long,userId:Long) = service.likePost(postId, userId).await()

    suspend fun unlikePost(postId:Long,userId:Long) = service.unlikePost(postId, userId).await()

    suspend fun getLikeCountByPostId(postId:Long) = service.getLikeCountByPostId(postId).await()

    suspend fun getLikeUserNamesByPostId(postId:Long) = service.getLikeUserNamesByPostId(postId).await()

    suspend fun createComment(postId:Long,userId:Long,content:String) = service.createComment(postId, userId, content).await()

    suspend fun getCommentsByPostId(postId:Long) = service.getCommentsByPostId(postId).await()
    suspend fun deleteComment(id:Long) = service.deleteComment(id).await()


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