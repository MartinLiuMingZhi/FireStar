package com.example.firestar.network

import com.example.firestar.data.LocationResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {

    @GET("/v3/ip")
    fun getLocation(@Query("key") key:String):Call<LocationResponse>
}