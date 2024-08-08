package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.firestar.data.RegisterRequest
import com.example.firestar.databinding.ActivityRegisterBinding
import com.example.firestar.network.NetWork
import com.example.firestar.network.TokenManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerButton = binding.codeBtn

        timerButton.setOnClickListener {
            startCountDown()
        }

        binding.register.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                register()
            }
        }

    }

    private fun startCountDown() {
        countDownTimer?.cancel()  // 取消之前的倒计时（如果有）

        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timerButton.text = "$secondsRemaining"
                timerButton.isEnabled = false
            }

            override fun onFinish() {
                timerButton.text = "获取验证码"
                timerButton.isEnabled = true
            }
        }.start()
    }

    private suspend fun register(){
        val email = binding.email.text.toString()
        val code = binding.code.text.toString()
        val password = binding.password.text.toString()
        val checkPassword = binding.checkPassword.text.toString()

        val response = NetWork.register(RegisterRequest(email, password, checkPassword, code))
        if (response.code == "200"){
            val token = response.data.token
            TokenManager.setCurrentToken(token)

            val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("token",token)
            editor.putString("username",response.data.username)
            editor.putString("email",response.data.email)
            editor.putString("avatar",response.data.avatar)
            editor.putBoolean("is_login", true)
            editor.apply()

            Looper.prepare()
            Toast.makeText(this,"注册成功", Toast.LENGTH_SHORT).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
            Looper.loop()
        }else{
            Looper.prepare()
            Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show()
            Looper.loop()
        }
    }
}

