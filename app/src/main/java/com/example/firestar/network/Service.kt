package com.example.firestar.network


import com.example.firestar.data.BaseResponse
import com.example.firestar.data.CommentDTO
import com.example.firestar.data.GetMessageResponse
import com.example.firestar.data.LoginRequest
import com.example.firestar.data.LoginResponse
import com.example.firestar.data.Page
import com.example.firestar.data.PostDTO
import com.example.firestar.data.RegisterRequest
import com.example.firestar.data.RegisterResponse
import com.example.firestar.data.SendMessageRequest
import com.example.firestar.data.UserPageResponse
import com.example.firestar.data.VerifyCodeRequest
import com.example.firestar.model.ContactItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Service {

    @POST("/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<BaseResponse<LoginResponse>>

    @POST("/user/register")
    fun register(@Body registerRequest: RegisterRequest):Call<BaseResponse<RegisterResponse>>

    @GET("/user/getUsers")
    fun getUsers():Call<BaseResponse<List<ContactItem>>>

    @GET("/user/page")
    fun userPages(@Body page: Page):Call<BaseResponse<UserPageResponse>>

    @GET("/mail/sendCode")
    fun sendCode(@Query("email") email:String):Call<BaseResponse<String>>

    @POST("/mail/verifyCode")
    fun verifyCode(@Body verifyCodeRequest: VerifyCodeRequest):Call<BaseResponse<String>>

    @POST("/messages")
    fun sendMessage(@Body sendMessageRequest: SendMessageRequest):Call<BaseResponse<String>>

    @GET("/messages")
    fun getMessage(@Query("senderId") senderId:Long,@Query("receiverId") receiverId:Long):Call<BaseResponse<GetMessageResponse>>


    @POST("/posts")
    fun createPost(@Query("userId") userId:Long,@Query("content") content:String):Call<BaseResponse<Boolean>>

    @GET("/posts")
    fun getAllPosts():Call<BaseResponse<List<PostDTO>>>

    @DELETE("/{id}")
    fun deletePost(@Path("id") id:Long):Call<BaseResponse<Boolean>>

    @POST("/likes")
    fun likePost(@Query("postId") postId:Long,@Query("userId") userId:Long):Call<BaseResponse<Boolean>>

    @DELETE("/likes")
    fun unlikePost(@Query("postId") postId:Long,@Query("userId") userId:Long):Call<BaseResponse<Boolean>>

    @GET("/likes/{postId}/count")
    fun getLikeCountByPostId(@Path("postId") postId:Long):Call<BaseResponse<Long>>

    @GET("/likes/{postId}/usernames")
    fun getLikeUserNamesByPostId(@Path("postId") postId:Long):Call<BaseResponse<String>>

    @POST("/comments")
    fun createComment(@Query("postId") postId:Long,@Query("userId") userId:Long,@Query("content") content:String):Call<BaseResponse<Boolean>>

    @GET("/comments/{postId}")
    fun getCommentsByPostId(@Path("postId") postId:Long):Call<BaseResponse<List<CommentDTO>>>

    @DELETE("/comments/{id}")
    fun deleteComment(@Path("id") id:Long):Call<BaseResponse<Boolean>>


}