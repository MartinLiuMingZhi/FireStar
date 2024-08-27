package com.example.firestar.data

import androidx.annotation.Size
import com.example.firestar.model.ContactItem
import java.sql.Timestamp

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

data class SendMessageRequest(
    val senderId: Long,
    val receiverId: Long,
    val content: String
)

data class GetMessageResponse(
    val id:Long,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val timestamp: Timestamp
)

data class UserDTO(
    val userid: Long,
    val username: String,
    val avatar: String,
    val sex: String,
    val email: String
)

data class Page(
    val page: Long,
    val pageSize: Long
)

data class UserPageResponse(
    val records: List<ContactItem>,
    val total: String,
    val size: String,
    val current: String,
    val pages: String
)