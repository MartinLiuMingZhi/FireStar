package com.example.firestar.network

import android.content.Context
import android.content.SharedPreferences
import com.example.firestar.App

object SharedPreferencesManager {
    private const val PREF_ACCOUNT_DATA = "account_data"
    private const val PREF_ACCOUNT = "account"

    // 获取对应的 SharedPreferences
    private fun getSharedPreferences(name: String): SharedPreferences {
        return App.getInstance().getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    // 保存值到 account_data
    fun saveAccountData(key: String, value: String) {
        val prefs = getSharedPreferences(PREF_ACCOUNT_DATA)
        prefs.edit().putString(key, value).apply()
    }

    fun saveAccountData(key: String, value: Boolean) {
        val prefs = getSharedPreferences(PREF_ACCOUNT_DATA)
        prefs.edit().putBoolean(key, value).apply()
    }

    // 从 account_data 获取值
    fun getAccountDataString(key: String, defaultValue: String): String? {
        val prefs = getSharedPreferences(PREF_ACCOUNT_DATA)
        return prefs.getString(key, defaultValue)
    }

    fun getAccountDataBoolean(key: String, defaultValue: Boolean): Boolean {
        val prefs = getSharedPreferences(PREF_ACCOUNT_DATA)
        return prefs.getBoolean(key, defaultValue)
    }

    // 保存值到 account
    fun saveAccountInfo(key: String, value: String) {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        prefs.edit().putString(key, value).apply()
    }

    fun saveAccountInfo(key: String, value: Boolean) {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        prefs.edit().putBoolean(key, value).apply()
    }

    fun saveAccountInfo(key: String, value: Long) {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        prefs.edit().putLong(key, value).apply()
    }

    // 从 account 获取值
    fun getAccountInfoString(key: String, defaultValue: String): String? {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        return prefs.getString(key, defaultValue)
    }

    fun getAccountInfoBoolean(key: String, defaultValue: Boolean): Boolean {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        return prefs.getBoolean(key, defaultValue)
    }

    fun getAccountInfoLong(key: String, defaultValue: Long): Long {
        val prefs = getSharedPreferences(PREF_ACCOUNT)
        return prefs.getLong(key, defaultValue)
    }

    // 清除数据
    fun clearAccount() {
        getSharedPreferences(PREF_ACCOUNT).edit().clear().apply()
    }

    fun clearAccountData() {
        getSharedPreferences(PREF_ACCOUNT_DATA).edit().clear().apply()
    }
}
