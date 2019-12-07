package com.example.blockchain;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

class updateTextView extends AsyncTask<String, String, Void> {
    StringBuilder useroutput = new StringBuilder();

    @Override
    protected Void doInBackground(String... strings) {
        publishProgress(strings);
        return null;
    }

    protected void onProgressUpdate(String text)
    {
        useroutput.append(text);
    }
}

public class MainActivity extends AppCompatActivity {

    private Button closeButton;
    public static Blockchain blockchain = new Blockchain(4);
    private TextView txtView;
    StringBuilder log=new StringBuilder();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("blockchain");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        txtView = new TextView(this);
        setContentView(R.layout.activity_main);
        this.closeButton = (Button) this.findViewById(R.id.stuff);
        this.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtView = (TextView) findViewById(R.id.txtView);
                txtView.setText("Sharath... It's working");
                txtView.setMovementMethod(new ScrollingMovementMethod());
                blockchain.addBlock(blockchain.newBlock("Block 1"));
                blockchain.addBlock(blockchain.newBlock("Block 2"));
                blockchain.addBlock(blockchain.newBlock("Block 3"));
                blockchain.addBlock(blockchain.newBlock("Block 4"));
                blockchain.addBlock(blockchain.newBlock("Block 5"));
                blockchain.addBlock(blockchain.newBlock("Block 6"));
                blockchain.addBlock(blockchain.newBlock("Block 7"));
                blockchain.addBlock(blockchain.newBlock("Block 8"));
                blockchain.addBlock(blockchain.newBlock("Block 9"));
                Boolean secondactivity = blockchain.addBlock(blockchain.newBlock("Block 10"));
                Block latestblock = blockchain.latestBlock();
                Block rogueblock = new Block(11, System.currentTimeMillis(),latestblock.getHash(), "New Block");
                secondactivity = blockchain.addBlock(rogueblock);
                log.append("\nNew Block to be added\n" + rogueblock);
                System.out.println("Blockchain valid ? " + blockchain.isBlockChainValid());
                log.append("Blockchain valid ? " + blockchain.isBlockChainValid() + "\n");
                System.out.println(blockchain);
                log.append("\nBlockchain at the end is " + blockchain + "\n");
                myRef.child("index").setValue(blockchain.latestBlock().index);
                myRef.child("timestamp").setValue(blockchain.latestBlock().timestamp);
                myRef.child("previous_hash").setValue(blockchain.latestBlock().previousHash);
                myRef.child("current_hash").setValue(blockchain.latestBlock().hash);
                myRef.child("data").setValue(blockchain.latestBlock().data);
                if (secondactivity == true) {
                    Intent in = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(in);
                }
                else {
                    txtView.setText(log);
                }

            }
        });
    }
}