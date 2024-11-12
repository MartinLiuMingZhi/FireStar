package com.example.firestar.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firestar.databinding.ActivityChatBinding
import com.example.websocket.WebSocketClient

class ChatActivity:AppCompatActivity(),WebSocketClient.MessageHandler {

    private lateinit var binding: ActivityChatBinding
    private lateinit var webSocketClient: WebSocketClient
    private val messages = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)



        webSocketClient = WebSocketClient(this)
    }
    override fun onMessageReceived(message: String) {
       runOnUiThread {
           messages.add(message)
           binding.recyclerChat.adapter?.notifyItemInserted(messages.size - 1)
       }
    }

    override fun onError(errorMessage: String) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

}