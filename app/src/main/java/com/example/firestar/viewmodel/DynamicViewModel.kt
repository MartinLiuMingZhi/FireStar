//package com.example.firestar.viewmodel
//
//import android.util.Log
//import androidx.core.net.ParseException
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.firestar.model.CommentItem
//import com.example.firestar.model.PostItem
//import com.example.firestar.network.NetWork
//import kotlinx.coroutines.async
//import kotlinx.coroutines.launch
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//
//class DynamicViewModel : ViewModel() {
//    private val _posts = MutableLiveData<List<PostItem>>()
//    val posts: LiveData<List<PostItem>> get() = _posts
//
//    // 加载帖子数据
//    fun loadPosts() {
//        viewModelScope.launch {
//            val response = NetWork.getAllPosts()
//
//            if (response.code == "200") {
//                // 并行处理每个帖子的相关网络请求
//                val posts = response.data.map { postDTO ->
//                    Log.d("DynamicViewModel", "Processing post with ID: ${postDTO.id}")
//
//                    // 并行获取评论、点赞数和用户信息
//                    val commentsDeferred = async { NetWork.getCommentsByPostId(postDTO.id) }
//                    val likeUsernamesDeferred = async { NetWork.getLikeUserNamesByPostId(postDTO.id) }
//                    val userDeferred = async { NetWork.getUserById(postDTO.userId) }
//
//                    val comments = commentsDeferred.await()
//                    val postLikeUsernames = likeUsernamesDeferred.await()
//                    val userById = userDeferred.await()
//
//                    // 构建评论列表
//                    val commentItems = if (comments.code == "200") {
//                        comments.data.mapNotNull { commentDTO ->
//                            Log.d("DynamicViewModel", "Comment UserId: ${commentDTO.userId}")
//                            val commentUserResponse = NetWork.getUserById(commentDTO.userId)
//
//                            if (commentUserResponse.code == "200") {
//                                CommentItem(
//                                    username = commentUserResponse.data.username,
//                                    content = commentDTO.content
//                                )
//                            } else {
//                                Log.d("DynamicViewModel", "Error loading user by ID")
//                                null
//                            }
//                        }
//                    } else {
//                        Log.e("DynamicViewModel", "Error loading comments")
//                        emptyList()
//                    }
//
//                    // 构建帖子信息
//                    PostItem(
//                        avatar = userById.data.avatar,
//                        username = userById.data.username,
//                        time = formatDateToDisplay(postDTO.createTime),
//                        content = postDTO.content,
//                        listLikeUsername = postLikeUsernames.data,
//                        commentList = commentItems
//                    )
//                }
//
//                // 更新 LiveData
//                _posts.postValue(posts)
//            } else {
//                Log.e("DynamicViewModel", "Error loading posts")
//            }
//        }
//    }
//
//    // 格式化日期
//    private fun formatDateToDisplay(dateString: String): String {
//        return try {
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
//            val date = inputFormat.parse(dateString)
//            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            outputFormat.format(date)
//        } catch (e: Exception) {
//            Log.e("DynamicViewModel", "Error parsing date", e)
//            dateString
//        }
//    }
//}