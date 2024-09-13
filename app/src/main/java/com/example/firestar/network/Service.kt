package com.example.firestar.network


import com.example.firestar.data.BaseResponse
import com.example.firestar.data.GetMessageResponse
import com.example.firestar.data.LoginRequest
import com.example.firestar.data.LoginResponse
import com.example.firestar.data.Page
import com.example.firestar.data.RegisterRequest
import com.example.firestar.data.RegisterResponse
import com.example.firestar.data.SendMessageRequest
import com.example.firestar.data.UserPageResponse
import com.example.firestar.data.VerifyCodeRequest
import com.example.firestar.model.ContactItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

}