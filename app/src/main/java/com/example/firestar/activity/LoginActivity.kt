package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.firestar.data.LoginRequest
import com.example.firestar.databinding.ActivityLoginBinding
import com.example.firestar.network.NetWork
import com.example.firestar.network.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity:AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
        val isRemember = sharedPreferences.getBoolean("remember_password",false)
        val isLogin = sharedPreferences.getBoolean("is_login",false)

        if (isLogin){
            //已经登录过，直接跳转到主界面
            val token = sharedPreferences.getString("token","")
            TokenManager.setCurrentToken(token.toString())
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            // 尚未登录，显示登录界面
            if (isRemember){
                val account = sharedPreferences.getString("email", "")
                val password = sharedPreferences.getString("password", "")
                binding.email.setText(account)
                binding.password.setText(password)
                binding.remember.isChecked = true
            }

            binding.login.setOnClickListener {
                val email = binding.email.text.toString()
                val password = binding.password.text.toString()
                val editor = sharedPreferences.edit()
                if (binding.remember.isChecked){
                    editor.putBoolean("remember_password",true)
                    editor.putString("email",email)
                    editor.putString("password",password)
                }
                else{
                    editor.clear()
                }
                editor.apply()
                CoroutineScope(Dispatchers.IO).launch {
                    login()
                }
            }

            binding.register.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
    }




    private suspend fun login(){
        try {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val loginResponse = NetWork.login(LoginRequest(email, password));
            if (loginResponse.code == "200"){
                val token = loginResponse.data.token
                TokenManager.setCurrentToken(token)

                val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("token",token)
                editor.putString("username",loginResponse.data.username)
                editor.putString("email",loginResponse.data.email)
                editor.putString("avatar",loginResponse.data.avatar)
                editor.putBoolean("is_login", true)
                editor.apply()

                Log.d("loginResponse:",loginResponse.toString());
                Looper.prepare()
                Toast.makeText(this@LoginActivity,"登录成功",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                Looper.loop()
            }else{
                Log.d("loginResponse:",loginResponse.toString());
                Looper.prepare()
                Toast.makeText(this@LoginActivity,"登录失败",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }catch (e:Exception){
            Log.e("TAG","login",e)
        }
    }
}