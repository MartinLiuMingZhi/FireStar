package com.example.firestar.network

import android.content.Context
import com.example.firestar.App

object TokenManager {

    private const val PREFS_NAME = "account"
    private const val TOKEN_KEY = "token"

    fun getCurrentToken(): String? {
        val prefs = App.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }

    fun setCurrentToken(token: String) {
        val prefs = App.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }

    fun clearToken() {
        val prefs = App.getInstance().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply()
    }
}

