package com.example.firestar.network


import com.example.firestar.data.BaseResponse
import com.example.firestar.data.LoginRequest
import com.example.firestar.data.LoginResponse
import com.example.firestar.data.RegisterRequest
import com.example.firestar.data.RegisterResponse
import com.example.firestar.data.VerifyCodeRequest
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

    @GET("/mail/sendCode")
    fun sendCode(@Query("email") email:String):Call<BaseResponse<String>>

    @POST("/mail/verifyCode")
    fun verifyCode(@Body verifyCodeRequest: VerifyCodeRequest):Call<String>
}