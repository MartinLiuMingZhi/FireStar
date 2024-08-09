package com.example.firestar.data

data class WeatherBaseResponse<T>(
    val code: String,
    val updateTime: String,
    val fxLink: String,
    val now: T
)

data class RealTimeResponse(
    val obsTime: String,
    val temp: String,
    val feelsLike: String,
    val icon: String,
    val text: String,
    val wind360: String,
    val winDir: String,
    val windScale: String,
    val windSpeed: String,
    val humidity: String,
    val precip: String,
    val pressure: String,
    val vis: String,
    val cloud: String,
    val dew: String
)
