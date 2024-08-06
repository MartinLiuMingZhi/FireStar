package com.example.firestar.network

object TokenManager {

    private var token: String ?= null

    fun setCurrentToken(newToken:String){
        token = newToken
    }

    fun getCurrentToken():String ?= token
}