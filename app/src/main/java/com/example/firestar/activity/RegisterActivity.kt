package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Looper
import android.text.TextUtils
import android.util.Log
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
import kotlinx.coroutines.withContext
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private val TAG = "RegisterActivity"

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
           CoroutineScope(Dispatchers.IO).launch {
               getCode()
           }
        }

        binding.register.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                register()
            }
        }

    }

    private suspend fun getCode(){

        try {
            val email = binding.email.text.toString().trim()
            if (!TextUtils.isEmpty(email)){
                if (checkEmail(email)){
                    // 切换到主线程来启动倒计时
                    withContext(Dispatchers.Main) {
                        startCountDown()
                    }
                    val response = NetWork.sendCode(email)
                    Log.d(TAG,response.toString())
                    // 切换到主线程显示 Toast
                    withContext(Dispatchers.Main) {
                        if (response.code == "200") {
                            Toast.makeText(this@RegisterActivity, "获取邮箱验证码成功", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, "获取邮箱验证码失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@RegisterActivity,"邮箱格式不正确",Toast.LENGTH_SHORT).show()
                    }

                }
            }else{
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegisterActivity,"请输入邮箱",Toast.LENGTH_SHORT).show()
                }
            }

        }catch (e:Exception){
            Log.e(TAG,"getCode",e)
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

            val sharedPreferences = getSharedPreferences("account", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putLong("userId",response.data.userid)
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

    private fun checkEmail(email:String):Boolean{
        val regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"
        val p = Pattern.compile(regex)
        val m = p.matcher(email)
        return m.matches()
    }
}

