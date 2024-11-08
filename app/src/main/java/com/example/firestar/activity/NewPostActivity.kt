package com.example.firestar.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.firestar.R
import com.example.firestar.databinding.ActivityNewPostBinding
import com.example.firestar.network.NetWork
import com.example.firestar.network.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding
    private val TAG = "NewPostActivity"
    private val RESULT_CODE_POST_SUCCESS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

         binding.publishPost.setOnClickListener {
             val content = binding.postContent.text.toString()
             val userId = SharedPreferencesManager.getAccountInfoLong("userId", 0)

             if (content.isNotEmpty() && userId != 0L ) {
                 try {
                     CoroutineScope(Dispatchers.IO).launch {
                         try {
                             val response = NetWork.createPost(userId, content)
                             withContext(Dispatchers.Main) {
                                 if (response.code == "200") {
                                     Log.d(TAG, "创建帖子成功")
                                     setResult(Activity.RESULT_OK)
                                     finish()
                                 } else {
                                     Log.d(TAG, "onCreate: ${response.toString()}")
                                 }
                             }
                         } catch (e: Exception) {
                             e.printStackTrace()
                             withContext(Dispatchers.Main) {
                                 Log.e(TAG, "网络请求失败: ${e.message}")
                             }
                         }
                     }
                 } catch (e: NumberFormatException) {
                     e.printStackTrace()
                     Log.e(TAG, "用户ID无效: ${e.message}")
                     // 显示错误提示给用户
                     runOnUiThread {
                         Toast.makeText(this, "用户ID无效，请重新登录", Toast.LENGTH_SHORT).show()
                     }
                 }
             } else {
                 if (content.isEmpty()) {
                     Toast.makeText(this, "内容不能为空", Toast.LENGTH_SHORT).show()
                 } else {
                     Toast.makeText(this, "用户ID为空，请重新登录", Toast.LENGTH_SHORT).show()
                 }
             }
         }


        binding.cancel.setOnClickListener {
            finish()
        }
    }
}