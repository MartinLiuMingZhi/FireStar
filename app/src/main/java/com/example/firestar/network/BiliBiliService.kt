package com.example.firestar.network

import com.example.firestar.data.BiliLoginResponse
import com.example.firestar.data.KeyHashResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface BiliBiliService {

    @POST("api/oauth2/getKey")
    suspend fun getKeyAndHash(): Call<KeyHashResponse>

    @FormUrlEncoded
    @POST("x/passport-login/web/key")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") encryptedPassword: String
    ): Call<BiliLoginResponse>


}