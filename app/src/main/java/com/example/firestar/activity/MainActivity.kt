package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.RoundedCorner
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.R
import com.example.firestar.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import kotlin.math.log

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
        val avatarSP = sharedPreferences.getString("avatar","")
        val emailSP = sharedPreferences.getString("email","")
        val usernameSP = sharedPreferences.getString("username","")

        val headerView = navView.getHeaderView(0)
        // 获取 header 中的控件
        val avatar = headerView.findViewById<ImageView>(R.id.avatar)
        val email = headerView.findViewById<TextView>(R.id.email)
        val username = headerView.findViewById<TextView>(R.id.username)

        email.text = emailSP
        username.text = usernameSP
        if (!avatarSP.isNullOrEmpty()) {
            Glide.with(this)
                .load(avatarSP)
                .apply(RequestOptions().transform(RoundedCorners(48)))
                .into(avatar)
        } else {
            // 设置默认图片
            avatar.setImageResource(R.drawable.ic_launcher_round)
        }

        val settingBtn = binding.btnSettings
        settingBtn.setOnClickListener {
            val edit = sharedPreferences.edit()
            edit.putBoolean("is_login",false)
            edit.remove("token")
            edit.remove("username")
            edit.remove("email")
            edit.remove("avatar")
            edit.apply()
            finishAffinity()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
