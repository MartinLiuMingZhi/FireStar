package com.example.firestar.data


data class LocationResponse(
    val status: Int,
    val info: String,
    val infocode: Int,
    val province: String,
    val city: String,
    val adcode: Int,
    val rectangle: String
)