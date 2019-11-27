package com.example.blockchain

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.blockchain.barcode.BarcodeCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONException
import org.json.JSONObject
import java.net.Socket


class SecondActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView
    private lateinit var database: DatabaseReference
    val blockchain_kotlin_copy: Blockchain = MainActivity.blockchain
    var listofips:ArrayList<String> = ArrayList()

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
            "key=" + "AAAA8b4Hjm4:APA91bFxLG_vPQFlzHMNwdEB0FCEgvIsRFl12kG3oxK6XRnIzDB3S7UUeb8s-EL3gZY4dpVNe0V-YrJIoOA7BPscfFG_O2ucl7lYCNNa9ykWI_1qDl7UAMPBjtUjDmgcKJuublXqXPFn"
    private val contentType = "application/json"


    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }


    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
                Response.Listener<JSONObject> { response ->
                    Log.i("TAG", "onResponse: $response")
                },
                Response.ErrorListener {
                    Toast.makeText(this@SecondActivity, "Request error", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)

        mResultTextView = findViewById(R.id.result_textview)

        findViewById<Button>(R.id.scan_barcode_button).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        database = FirebaseDatabase.getInstance().reference
        val s: Socket
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    println(barcode.displayValue)
                    val ip = barcode.displayValue.split("|||")[0]
                    val barcode_real = barcode.displayValue.split("|||")[1]
                    println(ip)
                    val block_to_be_added = Block(blockchain_kotlin_copy.latestBlock().getIndex() + 1, System.currentTimeMillis(), barcode_real, "Rogue Block")
                    println(block_to_be_added)
                    val tf = blockchain_kotlin_copy.addBlock(block_to_be_added)
                    println(blockchain_kotlin_copy)
                    val manager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val info = manager.connectionInfo
                    val address = info.macAddress
//                    FirebaseInstanceId.getInstance().instanceId
//                            .addOnCompleteListener(OnCompleteListener { task ->
//                                if (!task.isSuccessful) {
//                                    Log.w(LOG_TAG, "getInstanceId failed", task.exception)
//                                    return@OnCompleteListener
//                                }
//
//                                // Get new Instance ID token
//                                val token = task.result?.token
//                                println("Token values are ")
//                                println(token)
////                                // Log and toast
////                                val msg = getString(R.string.msg_token_fmt, token)
////                                Log.d(TAG, msg)
////                                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                            })
                    println("tf = ${tf}")
                    if (tf == true) {
                        database.child("Latest Hash").setValue(block_to_be_added.getHash())
                        database.child("IP addresses").child(address).setValue(ip)
                        println("Coming here to check it")
                        println(ip)
                        val topic = "/topics/blockchain" //topic has to match what the receiver subscribed to

                        val notification = JSONObject()
                        val notificationBody = JSONObject()

                        try {
                            notificationBody.put("title", "BlockchainCopy")
                            notificationBody.put("message", blockchain_kotlin_copy)   //Enter your notification message
                            notification.put("to", topic)
                            notification.put("data", notificationBody)
                            Log.e("TAG", "try")
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }
                        println(notification)
                        sendNotification(notification)
//                        val topic =  "highScores";
//                        val fm = FirebaseMessaging.getInstance()
//                        fm.send(RemoteMessage.Builder("$1038275284590@fcm.googleapis.com")
//                                .setMessageId(Integer.toString(100))
//                                .addData("my_message", "Hello World")
//                                .addData("my_action", "SAY_HELLO")
//                                .build())
                        // val intent = Intent(this, MyService::class.java)
                        // this.startService(intent)
                        // FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

                    }
                    mResultTextView.text = barcode_real
                } else
                    mResultTextView.setText(R.string.no_barcode_captured)
            } else
                Log.e(LOG_TAG, String.format(getString(R.string.barcode_error_format),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val BARCODE_READER_REQUEST_CODE = 1
    }
}
