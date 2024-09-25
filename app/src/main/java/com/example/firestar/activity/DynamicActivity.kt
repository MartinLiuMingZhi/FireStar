package com.example.firestar.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.R
import com.example.firestar.adapter.PostAdapter
import com.example.firestar.data.PostDTO
import com.example.firestar.databinding.ActivityDynamicBinding
import com.example.firestar.model.CommentItem
import com.example.firestar.model.PostItem
import com.example.firestar.network.NetWork
import com.example.firestar.network.SharedPreferencesManager
import kotlinx.coroutines.launch

class DynamicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDynamicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDynamicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.userName.text  = SharedPreferencesManager.getAccountDataString("userName", "")
        Glide.with(this).load(SharedPreferencesManager.getAccountDataString("userAvatar", "")).apply(
            RequestOptions().transform(RoundedCorners(48))).into(binding.userAvatar)

        // 加载帖子
        loadPosts()

    }

    // 异步加载帖子数据
    private fun loadPosts() {
        lifecycleScope.launch {
            try {
                // 获取所有帖子
                val response = NetWork.getAllPosts()
                if (response.data.isNullOrEmpty()) {
                    Log.d("DynamicActivity", "No posts found")
                } else {
                    // 转换 PostDTO 为 PostItem
                    val posts = response.data.map { postDTO ->
                        // 异步获取评论数据
                        val commentsResponse = NetWork.getCommentsByPostId(postDTO.id)
                        val comments = commentsResponse.data?.map { commentDTO ->
                            CommentItem(
                                username = "User ${commentDTO.userId}", // 假设这里根据 userId 获取 username
                                content = commentDTO.content
                            )
                        } ?: emptyList()

                        // 构建 PostItem
                        PostItem(
                            avatar = "https://example.com/avatar/${postDTO.userId}", // 假设这里根据 userId 获取头像
                            username = "User ${postDTO.userId}", // 假设这里根据 userId 获取 username
                            time = postDTO.createTime.toString(), // 格式化时间
                            content = postDTO.content,
                            listLikeUsername = "User1, User2", // 这里需要根据实际数据设置点赞用户列表
                            commentList = comments
                        )
                    }
                    setupRecyclerView(posts)
                }
            } catch (e: Exception) {
                Log.e("DynamicActivity", "Error loading posts", e)
            }
        }
    }

    // 设置 RecyclerView
    private fun setupRecyclerView(posts: List<PostItem>) {
        val postAdapter = PostAdapter(posts)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DynamicActivity)
            adapter = postAdapter
        }
    }
}