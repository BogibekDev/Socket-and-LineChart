package dev.matyaqubov.socketinandroid.manager

import android.util.Log
import com.google.gson.Gson
import dev.matyaqubov.socketinandroid.model.BitCoin
import dev.matyaqubov.socketinandroid.model.Connect
import dev.matyaqubov.socketinandroid.model.DataSend
import okhttp3.*
import okio.ByteString

class WebSocketBts {
    var mWebSocket: WebSocket? = null
    lateinit var socketListener: SocketListener
    private var gson = Gson()

    fun connectToSocket() {
        val client = OkHttpClient()
        val connect = Connect(DataSend("live_trades_btcusd"), "bts:subscribe")
        val message= Gson().toJson(connect)
        val request: Request = Request.Builder().url("wss://ws.bitstamp.net").build()
        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                mWebSocket = webSocket
                webSocket.send(message)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                Log.d("TAG", "receiving : $text ")
                val bitCoin = gson.fromJson(text, BitCoin::class.java)
                socketListener.onSuccess(bitCoin)

            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                //for image video
                Log.d("TAG", "Receiving bytes: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("TAG", "onClosing: $code / $reason")
//                webSocket.close(1000,null)
//                webSocket.cancel()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("TAG", "Error: ${t.message}")
                socketListener.onFailure(t.localizedMessage)
            }
        })
        client.dispatcher().executorService().shutdown()
    }

    fun socketListener(socketListener: SocketListener) {
        this.socketListener = socketListener
    }
}

interface SocketListener {
    fun onSuccess(bitCoin: BitCoin)
    fun onFailure(message: String)
}