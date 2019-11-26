package com.example.blockchain_ticketing;

import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class T_Client extends Thread {
    private static final String TAG = "T_Client";

    private Socket sock = null;
    private boolean running = false;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Object objIn;

    public void send(String _msg) {
        if (out != null) {
            try {
                out.writeObject(_msg);
                out.flush();
                Log.i("Send Method", "Outgoing : " + _msg.toString());
            } catch (IOException ex) {
                Log.e("Send Method", ex.toString());
            }
        }
    }

    public void stopClient() {
        Log.v(TAG,"stopClient method run");
        running = false;
    }

    @Override
    public void run() {
        running = true;
        try {
            ServerSocket sock1 = new ServerSocket(9999);
            try {
                Log.i(TAG, "C: Connected.");
                while (running) {
                    sock = sock1.accept();
                    out = new ObjectOutputStream(sock.getOutputStream());
                    in = new ObjectInputStream(sock.getInputStream());
                    objIn = in.readObject();
                    Log.i("Object Read Class", objIn.getClass().toString());
                    Log.i("Object Read", objIn.toString());
                    if (objIn != null) {
                        Intent dialogIntent = new Intent();
                        dialogIntent.setClass(this, MainActivity.class);
                        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                    }
                    System.out.println("Object Read Class" + objIn.getClass().toString());
                    System.out.println("Object Read" + objIn.toString());
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + objIn + "'");
            } catch (Exception e) {
                Log.e(TAG, "S: Error", e);
            } finally {
                out.close();
                in.close();
                sock.close();
                Log.i(TAG, "Closing socket: " + sock);
            }
        } catch (Exception e) {
            Log.e(TAG, "C: Error", e);
        }
    }
}
