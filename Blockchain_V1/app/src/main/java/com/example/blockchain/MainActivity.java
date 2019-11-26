package com.example.blockchain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Thread.sleep;

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
    private EditText hashInput;
    StringBuilder log=new StringBuilder();

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
                hashInput = (EditText) findViewById(R.id.HashInput);
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
                // log.append(blockchain + "\n");

                /*
                // add an invalid block to corrupt Blockchain
                Block latestblock = blockchain.latestBlock();
                Block rogueblock = new Block(11, System.currentTimeMillis(),"aabbbabba", "Rogue Block");
                Boolean secondactivity = blockchain.addBlock(rogueblock);
                log.append("\nNew Block to be added\n" + rogueblock);

                // Boolean secondactivity = blockchain.isBlockChainValid();

                System.out.println("Blockchain valid ? " + blockchain.isBlockChainValid());
                log.append("\nBlockchain valid ? " + blockchain.isBlockChainValid() + "\n");
                */

                log.append("\nBlockchain at the end is " + blockchain + "\n");
                try {
                    sleep(8000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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