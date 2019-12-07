package com.example.blockchain

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.blockchain.barcode.BarcodeCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.database.*
import org.json.JSONException
import org.json.JSONObject
import java.net.Socket
import java.util.*
import kotlin.collections.ArrayList

@Keep
class Test constructor() {
    val current_hash: String = ""
    val timestamp: Long = -1
    val previous_hash: String = ""
    val data: String = ""
    val index: Int = -1

}

@Keep
class SecondActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView
    private lateinit var database: DatabaseReference
    var blockchain_kotlin_copy: Blockchain = MainActivity.blockchain
    var blockchain_new: Blockchain = Blockchain(4)
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
        database = FirebaseDatabase.getInstance().reference
        mResultTextView = findViewById(R.id.result_textview)


        val postListener  = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val stuff = dataSnapshot.getValue(Test::class.java)
                println(stuff!!.index)
                println(blockchain_kotlin_copy.latestBlock().index)
                if (stuff!!.index != blockchain_kotlin_copy.latestBlock().index) {
                    val block = Block(stuff!!.index, stuff!!.timestamp, stuff!!.previous_hash, stuff!!.data);
                    println("Block now is "+block)
                    blockchain_kotlin_copy.addBlock(block)
                    println("Blockchain now is "+blockchain_kotlin_copy)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        database.child("blockchain").addValueEventListener(postListener)

        findViewById<Button>(R.id.scan_barcode_button).setOnClickListener {
            val intent = Intent(applicationContext, BarcodeCaptureActivity::class.java)
            startActivityForResult(intent, BARCODE_READER_REQUEST_CODE)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val LOG_TAG = MainActivity::class.java.simpleName
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
                    println("Important debugging stuff here")
                    println(blockchain_kotlin_copy.getDifficulty())
                    println(blockchain_kotlin_copy.latestBlock().getHash())
                    val block_to_be_added = Block(blockchain_kotlin_copy.latestBlock().getIndex() + 1, System.currentTimeMillis(), barcode_real,"SecondActivity")
                    println("Block to be added is : ");
                    println(block_to_be_added)
                    val tf = blockchain_kotlin_copy.addBlock(block_to_be_added)
                    println(blockchain_kotlin_copy)
                    val manager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
                    val info = manager.connectionInfo
                    val address = info.macAddress
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
                            notificationBody.put("ip-address", ip)
                            notification.put("data", notificationBody)
                            Log.e("TAG", "try")
                        } catch (e: JSONException) {
                            Log.e("TAG", "onCreate: " + e.message)
                        }
                        database.child("blockchain").child("index").setValue(blockchain_kotlin_copy.latestBlock().index);
                        database.child("blockchain").child("current_hash").setValue(blockchain_kotlin_copy.latestBlock().hash);
                        database.child("blockchain").child("previous_hash").setValue(blockchain_kotlin_copy.latestBlock().previousHash);
                        database.child("blockchain").child("timestamp").setValue(blockchain_kotlin_copy.latestBlock().timestamp);
                        database.child("blockchain").child("data").setValue(blockchain_kotlin_copy.latestBlock().data);
                        println(notification)
                        sendNotification(notification)
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


fun sendPushToSingleInstance(activity: Context, dataValue: Blockchain, instanceIdToken: String?) {


    val url = "https://fcm.googleapis.com/fcm/send"
    val myReq = object : StringRequest(Request.Method.POST, url,
            Response.Listener { Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show() },
            Response.ErrorListener { Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show() }) {

        @Throws(com.android.volley.AuthFailureError::class)
        override fun getBody(): ByteArray {
            val rawParameters = Hashtable<Any, Any>()
            rawParameters.put("data", dataValue)
            rawParameters.put("to", instanceIdToken)
            return JSONObject(rawParameters).toString().toByteArray()
        }

        override fun getBodyContentType(): String {
            return "application/json; charset=utf-8"
        }

        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map<String, String> {
            val headers = HashMap<String, String>()
            headers["Authorization"] = "key=AIzaSyAhUz5F-fDBLGlQ1UZh1bm-GqbKou-FIvA"
            headers["Content-Type"] = "application/json"
            return headers
        }

    }

    Volley.newRequestQueue(activity).add(myReq)
}