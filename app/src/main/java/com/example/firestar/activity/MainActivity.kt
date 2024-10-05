package com.example.firestar.activity

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.firestar.R
import com.example.firestar.databinding.ActivityMainBinding
import com.example.firestar.model.getSky
import com.example.firestar.network.KeyManager
import com.example.firestar.network.WeatherNetWork
import com.example.firestar.viewmodel.LocationViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity(), AMapLocationListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var location: String

    private val locationViewModel: LocationViewModel by viewModels()

    private val TAG = "MainActivity"

    // 请求权限意图
    private lateinit var requestPermission: ActivityResultLauncher<String>

    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null

    //声明AMapLocationClientOption对象
    var mLocationOption: AMapLocationClientOption? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

//        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化权限请求启动器
        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Log.d(TAG, "权限已授予")
                // 在此处理权限授予后的操作
            } else {
                Log.d(TAG, "权限被拒绝")
                Toast.makeText(this, "定位权限被拒绝，部分功能可能无法使用", Toast.LENGTH_SHORT).show()
            }
        }

        //初始化定位
        initLocation()


        setSupportActionBar(binding.appBarMain.toolbar)

//        binding.appBarMain.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_map, R.id.nav_video,R.id.nav_chat,R.id.nav_bilibili
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val sharedPreferences = getSharedPreferences("account_data", Context.MODE_PRIVATE)
        val sharedPreferencesAccount = getSharedPreferences("account",Context.MODE_PRIVATE)

        val avatarSP = sharedPreferencesAccount.getString("avatar","")
        val emailSP = sharedPreferencesAccount.getString("email","")
        val usernameSP = sharedPreferencesAccount.getString("username","")

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
            val edit = sharedPreferencesAccount.edit()
            edit.clear()
            edit.apply()
            finishAffinity()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.icWeather.setOnClickListener {
            val intent  = Intent(this,WeatherActivity::class.java)
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

    override fun onResume() {
        super.onResume()
        // 检查是否已经获取到定位权限
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onResume: 已获取到权限")
            startLocation()

        } else {
            // 请求定位权限
            requestPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /**
     * 初始化定位
     */
    private fun initLocation() {
        try {
            //初始化定位
            mLocationClient = AMapLocationClient(applicationContext)
            //设置定位回调监听
            mLocationClient!!.setLocationListener(this)

            //初始化AMapLocationClientOption对象

            //初始化AMapLocationClientOption对象
            mLocationOption = AMapLocationClientOption()
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption!!.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            //获取最近3s内精度最高的一次定位结果
            mLocationOption!!.setOnceLocationLatest(true)
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption!!.setNeedAddress(true)
            //设置定位超时时间，单位是毫秒
            mLocationOption!!.setHttpTimeOut(6000)
            //给定位客户端对象设置定位参数
            mLocationClient!!.setLocationOption(mLocationOption)


        } catch (e: java.lang.Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 开始定位
     */
    private fun startLocation() {
        mLocationClient?.startLocation()
    }

    /**
     * 停止定位
     */
    private fun stopLocation() {
        mLocationClient?.stopLocation()
    }


    /**
     * 实现 AMapLocationListener 接口的 onLocationChanged 方法
     */
    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {

                // 更新 ViewModel 中的位置信息
                locationViewModel.setLocation(aMapLocation)

                // 定位成功，更新UI
                val locationStr = "${aMapLocation.city}, ${aMapLocation.district}"
                binding.location.text = aMapLocation.district

                // 获取城市代码
                val cityCode = aMapLocation.cityCode
                Log.d("Location", "City Code: $cityCode")

                //获取中国行政区域编码
                val adcode = aMapLocation.adCode
                Log.d("adcode","adcode:$adcode")

                // 获取经纬度
                val latitude = aMapLocation.latitude
                val longitude = aMapLocation.longitude
                Log.d("Location", "Latitude: $latitude, Longitude: $longitude")


                location = "$longitude,$latitude"
                Log.d("location",location)

                // 确保在 location 被赋值后调用 refreshWeather
                CoroutineScope(Dispatchers.IO).launch {
                    refreshWeather()
                }
            } else {
                // 定位失败，显示错误信息
                Log.e(TAG, "定位失败，错误码: ${aMapLocation.errorCode}, 错误信息: ${aMapLocation.errorInfo}")
            }
        }
    }


    private suspend fun refreshWeather() {
        val weatherResponse = WeatherNetWork.getRealtimeWeather(KeyManager.WEATHERKEY, location)
        if (weatherResponse.code == "200") {
            withContext(Dispatchers.Main) {
                binding.icWeather.setImageResource(getSky(weatherResponse.now.icon).icon)
                binding.temperature.text = "${weatherResponse.now.temp}℃"
                binding.weatherText.text = weatherResponse.now.text
            }
        } else {
            Log.d(TAG, "获取天气信息失败")
        }
    }

}
