package com.example.mymassenger

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class MainActivity : AppCompatActivity() {
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var uploadButton: Button
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        uploadButton = findViewById(R.id.uploadButton)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            sendMessage(message)
        }

        uploadButton.setOnClickListener {
            // Логика для загрузки файла
        }

        initializeWebSocket()
    }

    private fun initializeWebSocket() {
        val client = OkHttpClient()
        val request = Request.Builder().url("ws://172.20.10.7:3000").build()
        webSocket = client.newWebSocket(request, WebSocketListenerImpl())
        client.dispatcher.executorService.shutdown()
    }

    private fun sendMessage(message: String) {
        webSocket?.send(message)
    }

    private inner class WebSocketListenerImpl : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            Log.d("WebSocket", "Connected to server")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("WebSocket", "Received: $text")
            runOnUiThread {
                // Обновление UI для отображения полученного сообщения
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("WebSocket", "Received bytes: ${bytes.hex()}")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Closing: $code / $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("WebSocket", "Closed: $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            Log.e("WebSocket", "Error: " + t.message, t)
        }
    }
}
