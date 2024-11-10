package com.example.firestar.data


import com.example.firestar.model.ContactItem
import java.util.Date

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
    val token:String,
    val status:Long
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
    val token: String,
    val status:Long
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
    val timestamp: String
)

data class UserDTO(
    val userid: Long,
    val username: String,
    val avatar: String,
    val sex: String,
    val email: String
)


data class UserPageResponse(
    val records: List<ContactItem>,
    val total: String,
    val size: String,
    val current: String,
    val pages: String
)

data class PostDTO(
    val id: Long,
    val userId: Long,
    val content: String,
    val createdTime: String,
    val updateTime: String
)

data class CommentDTO(
    val id: Long,
    val userId: Long,
    val postId: Long,
    val content: String,
    val createTime: String,
)