package com.example.firestar.model

data class PostItem(
    val avatar: String,
    val username: String,
    val time: String,
    val content: String,
    val listLikeUsername:String,
    val commentList:List<CommentItem>

)