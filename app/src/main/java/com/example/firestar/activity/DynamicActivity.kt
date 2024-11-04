package com.example.firestar.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.adapter.PostAdapter
import com.example.firestar.databinding.ActivityDynamicBinding
import com.example.firestar.model.PostItem
import com.example.firestar.network.SharedPreferencesManager
import com.example.firestar.viewmodel.DynamicViewModel



class DynamicActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDynamicBinding

    private val viewModel: DynamicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDynamicBinding.inflate(layoutInflater)
        setContentView(binding.root)


       setupUI()

        // 观察帖子数据变化
        viewModel.posts.observe(this, Observer { posts ->
            setupRecyclerView(posts)
        })

        // 加载帖子
        viewModel.loadPosts()

    }

    // 设置用户界面
    private fun setupUI() {
        binding.userName.text = SharedPreferencesManager.getAccountInfoString("username", "")
        val userAvatarUrl = SharedPreferencesManager.getAccountInfoString("avatar", "")
        Glide.with(this)
            .load(userAvatarUrl)
            .apply(RequestOptions().transform(RoundedCorners(48)))
            .into(binding.userAvatar)
        Log.d("setupUI", "userAvatarUrl: $userAvatarUrl")
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
