package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.amap.api.location.AMapLocationClient
import com.example.firestar.data.LoginRequest
import com.example.firestar.databinding.ActivityLoginBinding
import com.example.firestar.network.NetWork
import com.example.firestar.network.RetrofitManager
import com.example.firestar.network.Service
import com.example.firestar.network.SharedPreferencesManager
import com.example.firestar.network.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity:AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 检查 token 是否有效
        checkLoginState()


    }
    /**
     * 检查登录状态和 token
     */
    private fun checkLoginState() {
        val token = TokenManager.getCurrentToken()
        val isLogin = SharedPreferencesManager.getAccountInfoBoolean("is_login", false)

        if (isLogin && !token.isNullOrEmpty()) {
            // 如果已经登录过，验证 token
            CoroutineScope(Dispatchers.IO).launch {
                val isValid = validateToken(token)
                withContext(Dispatchers.Main) {
                    if (isValid) {
                        // token 有效，跳转到主界面
                        navigateToMainActivity()
                    } else {
                        // token 无效，清除登录信息并显示登录界面
                        handleInvalidToken()
                    }
                }
            }
        } else {
            // 尚未登录或没有 token，显示登录界面
            showLoginScreen()
        }
    }

    /**
     * 调用服务器接口验证 token 有效性
     */
    private suspend fun validateToken(token: String): Boolean {
        return try {
            // 使用一个受保护的 API 来验证 token，比如获取用户信息
            val response = NetWork.getUsers()
            response.code == "200"
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 跳转到主界面
     */
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * 处理 token 无效
     */
    private fun handleInvalidToken() {
        TokenManager.clearToken()
        SharedPreferencesManager.clearAccount()
        showLoginScreen()
    }

    /**
     * 显示登录界面
     */
    private fun showLoginScreen() {
        val isRemember = SharedPreferencesManager.getAccountDataBoolean("remember_password", false)

        if (isRemember) {
            val account = SharedPreferencesManager.getAccountDataString("email", "")
            val password = SharedPreferencesManager.getAccountDataString("password", "")
            binding.email.setText(account)
            binding.password.setText(password)
            binding.remember.isChecked = true
        }

        binding.login.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                login()
            }
        }

        binding.register.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * 执行登录操作
     */
    private suspend fun login() {
        try {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val loginResponse = NetWork.login(LoginRequest(email, password))

            if (loginResponse.code == "200") {
                val token = loginResponse.data.token
                TokenManager.setCurrentToken(token)

                // 保存账户信息
                SharedPreferencesManager.saveAccountInfo("token", token)
                SharedPreferencesManager.saveAccountInfo("username", loginResponse.data.username)
                SharedPreferencesManager.saveAccountInfo("email", loginResponse.data.email)
                SharedPreferencesManager.saveAccountInfo("avatar", loginResponse.data.avatar)
                SharedPreferencesManager.saveAccountInfo("is_login", true)

                if (binding.remember.isChecked) {
                    SharedPreferencesManager.saveAccountData("email", email)
                    SharedPreferencesManager.saveAccountData("password", password)
                    SharedPreferencesManager.saveAccountData("remember_password", true)
                } else {
                    SharedPreferencesManager.clearAccountData()
                    SharedPreferencesManager.saveAccountData("remember_password", false)
                }

                // UI 操作需要在主线程中执行
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }

            } else {
                // 登录失败，提示用户
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "login error", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@LoginActivity, "登录异常", Toast.LENGTH_SHORT).show()
            }
        }
    }
}