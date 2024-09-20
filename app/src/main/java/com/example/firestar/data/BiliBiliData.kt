package com.example.firestar.data

data class KeyHashResponse(
    val code: Int,
    val message: String,
    val ttl: Int,
    val data: KeyHashData
)

data class KeyHashData(
    val hash: String,
    val key: String
)

data class BiliLoginResponse(
    val code: Int,
    val message: String,
    val data: LoginData
)

data class LoginData(
    val token: String,
    // 其他登录相关信息
)