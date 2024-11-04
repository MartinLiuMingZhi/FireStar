package com.example.firestar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firestar.model.CommentItem
import com.example.firestar.model.PostItem
import com.example.firestar.network.NetWork
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DynamicViewModel : ViewModel() {
    private val _posts = MutableLiveData<List<PostItem>>()
    val posts: LiveData<List<PostItem>> get() = _posts

    // 加载帖子数据
    fun loadPosts() {
        viewModelScope.launch {
            val response = NetWork.getAllPosts()

            if (response.code == "200") {
                val posts = response.data.mapNotNull { postDTO ->
                    Log.d("DynamicViewModel", "Processing post with ID: ${postDTO.id}")

                    // 使用 try-catch 捕获每个网络请求中的异常
                    val comments = try {
                        NetWork.getCommentsByPostId(postDTO.id)
                    } catch (e: Exception) {
                        Log.e("DynamicViewModel", "Error loading comments", e)
                        null
                    }

                    val postLikeUsernames = try {
                        NetWork.getLikeUserNamesByPostId(postDTO.id)
                    } catch (e: Exception) {
                        Log.e("DynamicViewModel", "Error loading likes", e)
                        null
                    }

                    val userById = try {
                        NetWork.getUserById(postDTO.userId)
                    } catch (e: Exception) {
                        Log.e("DynamicViewModel", "Error loading user info", e)
                        null
                    }

                    if (userById == null || userById.code != "200") {
                        Log.e("DynamicViewModel", "User info load failed for user ID: ${postDTO.userId}")
                        return@mapNotNull null
                    }

                    // 构建评论列表
                    val commentItems = if (comments?.code == "200") {
                        comments.data.mapNotNull { commentDTO ->
                            val commentUserResponse = try {
                                NetWork.getUserById(commentDTO.userId)
                            } catch (e: Exception) {
                                Log.e("DynamicViewModel", "Error loading user for comment", e)
                                null
                            }
                            if (commentUserResponse?.code == "200") {
                                CommentItem(
                                    username = commentUserResponse.data.username ?: "",
                                    content = commentDTO.content
                                )
                            } else {
                                null
                            }
                        }
                    } else emptyList()

                    // 构建帖子信息
                    PostItem(
                        avatar = userById.data.avatar ?: "",
                        username = userById.data.username ?: "",
                        time = formatDateToDisplay(postDTO.createdTime),
                        content = postDTO.content,
                        listLikeUsername = postLikeUsernames?.data ?: "",
                        commentList = commentItems
                    )
                }

                // 更新 LiveData
                _posts.postValue(posts)
            } else {
                Log.e("DynamicViewModel", "Error loading posts or no posts data")
            }
        }
    }

    private fun formatDateToDisplay(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("DynamicViewModel", "Error parsing date", e)
            dateString
        }
    }
}