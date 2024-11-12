package com.example.websocket

import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebSocketClient(private val messageHandler: MessageHandler) {

    private val SERVER_URL = "ws://8.134.79.156:7688/ws" // WebSocket 服务器的 URL
    private val PING_INTERVAL = 30L // 心跳间隔时间，单位秒
    private val CLIENT = OkHttpClient.Builder()
        .pingInterval(PING_INTERVAL, TimeUnit.SECONDS)
        .build()

    private var webSocket: WebSocket? = null

    init {
        connect()
    }

    private fun connect() {
        val request = Request.Builder().url(SERVER_URL).build()
        webSocket = CLIENT.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocketClient", "WebSocket connected")
                subscribeToMessages()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocketClient", "Receiving : $text")
                messageHandler.onMessageReceived(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocketClient", "Receiving bytes : ${bytes.hex()}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, null)
                Log.d("WebSocketClient", "Closing : $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocketClient", "Error : ${t.message}")
                messageHandler.onError(t.message ?: "Unknown error")
            }
        })
    }

    private fun subscribeToMessages() {
        val subscribeMessage = "{\"destination\":\"/topic/messages\",\"id\":\"sub-0\"}"
        webSocket?.send(subscribeMessage)?.let {
            Log.d("WebSocketClient", "Subscription message sent")
        } ?: run {
            Log.e("WebSocketClient", "Failed to send subscription message")
        }
    }

    fun sendMessage(message: String) {
        try {
            val jsonMessage = JSONObject().apply {
                put("senderId", 1) // 替换为实际的发送者 ID
                put("receiverId", 2) // 替换为实际的接收者 ID
                put("content", message)
            }
            val stompMessage = "SEND /app/chat\n" +
                    "content-type:application/json\n" +
                    "\n" +
                    jsonMessage.toString() + "\u0000"
            webSocket?.send(stompMessage)?.let {
                Log.d("WebSocketClient", "Message sent: $message")
            } ?: run {
                Log.e("WebSocketClient", "Failed to send message: $message")
            }
        } catch (e: Exception) {
            Log.e("WebSocketClient", "Exception while sending message: ${e.message}")
            messageHandler.onError("Exception while sending message: ${e.message}")
        }
    }

    fun close() {
        webSocket?.close(1000, "Goodbye!")?.let {
            Log.d("WebSocketClient", "WebSocket closed")
        } ?: run {
            Log.e("WebSocketClient", "Failed to close WebSocket")
        }
    }

    interface MessageHandler {
        fun onMessageReceived(message: String)
        fun onError(errorMessage: String)
    }
}
