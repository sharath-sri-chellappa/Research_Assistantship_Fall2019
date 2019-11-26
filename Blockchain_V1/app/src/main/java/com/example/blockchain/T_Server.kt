package com.example.blockchain

import android.util.Log

import java.io.IOException
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket

internal class T_Server : Thread() {

    private var sock: Socket? = null
    private var running = false
    private var out: ObjectOutputStream? = null
    private val objIn: Any? = null
    var blockchain_kotlin_copy = SecondActivity().blockchain_kotlin_copy
    fun send(_msg: String) {
        if (out != null) {
            try {
                out!!.writeObject(_msg)
                out!!.flush()
                Log.i("Send Method", "Outgoing : $_msg")
            } catch (ex: IOException) {
                Log.e("Send Method", ex.toString())
            }

        }
    }

    fun stopClient() {
        Log.v(TAG, "stopClient method run")
        running = false
    }

    override fun run() {
        running = true
        try {
            val sock1 = ServerSocket(9999)
            try {
                Log.i(TAG, "C: Connected.")
                while (running) {
                    sock = sock1.accept()
                    try {
                    out = ObjectOutputStream(sock!!.getOutputStream())
                    out!!.writeObject(blockchain_kotlin_copy)
                    out!!.flush()
                    out!!.reset()
                    Log.i("Send Method", "Outgoing : $blockchain_kotlin_copy")
                    println("Out is being sent")
                    println("$blockchain_kotlin_copy")
                } catch (ex: IOException) {
                        Log.e("Send Method", ex.toString())
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "S: Error", e)
            } finally {
                out!!.close()
                sock!!.close()
                Log.i(TAG, "Closing socket: " + sock!!)
            }
        } catch (e: Exception) {
            Log.e(TAG, "C: Error", e)
        }

    }

    companion object {
        private val TAG = "T_Server"
    }
}
