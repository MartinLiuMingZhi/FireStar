package com.example.firestar.utils

import com.google.firebase.BuildConfig

//获取版本号
object VersionUtil {

    fun getVersion(): String {
//        val versionCOde = BuildConfig.VERSION_CODE
        val versionName = BuildConfig.VERSION_NAME
        val version = versionName + ""
        return version
    }
}