package com.example.blockchain_ticketing

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import com.google.gson.JsonObject
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import org.json.JSONObject

class Test constructor() {
    val current_hash: String = ""
    val timestamp: Long = -1
    val previous_hash: String = ""
    val data: String = ""
    val index: Int = -1

}



class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"
    private lateinit var database: DatabaseReference
    var blockchain: Blockchain? = Blockchain(4)
    fun getLocalIpAddress(): String? {
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf = en.nextElement()
                val enumIpAddr = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.getHostAddress()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }

        return null
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        database = FirebaseDatabase.getInstance().reference
        super.onMessageReceived(p0)
        Log.e("TAG", "Useless Shit")
        println("RemoteMessage")
        val intent = Intent(this, SecondActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them.
      */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
                resources,
                R.drawable.ic_delete
        )

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_delete)
                .setLargeIcon(largeIcon)
                .setContentTitle(p0?.data?.get("title"))
                .setContentText(p0?.data?.get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent)

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = resources.getColor(R.color.background_dark)
        }
        val local_ip = getLocalIpAddress()
        if (local_ip == p0?.data?.get("ip-address").toString()) {
            notificationManager.notify(notificationID, notificationBuilder.build())
            Log.e("TAG", "Baroamma")
            /*val postListener  = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val stuff = dataSnapshot.getValue(Test::class.java)
                    val block = Block(stuff!!.index+1, System.currentTimeMillis(), stuff!!.current_hash, "New Block");
                    blockchain!!.addBlock(block)
                    println(blockchain)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled ${databaseError.toException()}")
                }
            }
            database.child("blockchain").addValueEventListener(postListener)
            print(blockchain)*/
            val inp = Intent(this, SecondActivity::class.java)
            inp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(inp)
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to devie notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}
