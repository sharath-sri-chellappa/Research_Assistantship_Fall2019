package com.example.blockchain_ticketing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.blockchain_ticketing.barcode.BarcodeCaptureActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SecondActivity : AppCompatActivity() {

    private lateinit var mResultTextView: TextView
    private lateinit var database: DatabaseReference
    val blockchain_kotlin_copy: Blockchain = MainActivity.blockchain

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
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    println(barcode.displayValue)
                    // val p = barcode.
                    val block_to_be_added = Block(blockchain_kotlin_copy.latestBlock().getIndex() + 1, System.currentTimeMillis(), barcode.displayValue, "Rogue Block")
                    println(block_to_be_added)
                    val tf = blockchain_kotlin_copy.addBlock(block_to_be_added)
                    println(blockchain_kotlin_copy)
                    println("tf = ${tf}")
                    if (tf == true)
                        database.child("Latest Hash").setValue(block_to_be_added.getHash())
                    mResultTextView.text = barcode.displayValue
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
