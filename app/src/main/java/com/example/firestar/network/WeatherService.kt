package com.example.firestar.network

import com.example.firestar.data.RealTimeResponse
import com.example.firestar.data.WeatherBaseResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/v7/weather/now")
    fun getRealtimeWeather(@Query("key") key:String,@Query("location") location:String):Call<WeatherBaseResponse<RealTimeResponse>>
}