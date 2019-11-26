package com.example.blockchain_ticketing;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
    private static final String TAG = "MyService";

    private T_Client client;

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy");
        if (client != null) {
            try {
                client.stopClient();
            } catch (Exception e) {
                Log.e(TAG, "Error on close: " + e);
            }
        }
        super.onDestroy();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        client = new T_Client();
        client.start();

        Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}