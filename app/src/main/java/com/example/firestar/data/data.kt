package com.example.firestar.data

data class BaseResponse<T>(
    val code: String,
    val msg:String,
    val data: T
)
data class LoginRequest(
    val email:String,
    val password:String
)

data class LoginResponse(
    val userid:Long,
    val username:String,
    val email: String,
    val avatar:String,
    val sex:String,
    val token:String
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val checkPassword:String,
    val code:String
)

data class RegisterResponse(
    val userid: Long,
    val username: String,
    val email: String,
    val avatar: String,
    val sex: String,
    val token: String
)

data class VerifyCodeRequest(
    val email: String,
    val code: String
)