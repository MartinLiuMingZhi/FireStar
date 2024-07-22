package com.example.firestar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firestar.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity:AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
        val isRemember = sharedPreferences.getBoolean("remember_password",false)
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
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

    }



    private suspend fun login(){
        try {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            if (email.equals("3108531642@qq.com")&&password.equals("12345678")){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Looper.prepare()
                Toast.makeText(this@LoginActivity,"登录失败",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }catch (e:Exception){
            Log.e("TAG","login",e)
        }
    }
}