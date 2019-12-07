package com.example.blockchain;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    public int difficulty;
    public List<Block> blocks;

    public Blockchain(int difficulty) {
        this.difficulty = difficulty;
        blocks = new ArrayList<>();
        // create the first block
        Block b = new Block(0, System.currentTimeMillis(), null, "First Block");
        b.mineBlock(difficulty);
        blocks.add(b);
    }

    public Blockchain(int difficulty, List<Block> blocks) {
        this.difficulty = difficulty;
        this.blocks = blocks;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Block latestBlock() {
        return blocks.get(blocks.size() - 1);
    }

    public Block newBlock(String data) {
        Block latestBlock = latestBlock();
        return new Block(latestBlock.getIndex() + 1, System.currentTimeMillis(),
                latestBlock.getHash(), data);
    }

    public Boolean addBlock(Block b) {
        if (b != null) {
            b.mineBlock(difficulty);
            blocks.add(b);
            if (isBlockChainValid())
            {
                System.out.println("Coming here : COUNT ");
                //  Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("Latest Hash").setValue(b.getHash());
                return true;
            }
            else
                return false;
        }
        return true;
    }

    public boolean isFirstBlockValid() {
        Block firstBlock = blocks.get(0);

        if (firstBlock.getIndex() != 0) {
            return false;
        }

        if (firstBlock.getPreviousHash() != null) {
            System.out.println("First block hash is screwed up");
            return false;
        }

        if (firstBlock.getHash() == null ||
                !Block.calculateHash(firstBlock).equals(firstBlock.getHash())) {
            System.out.println("First block hash is screwed up");
            return false;
        }

        return true;
    }

    public boolean isValidNewBlock(Block newBlock, Block previousBlock) {
        if (newBlock != null  &&  previousBlock != null) {
            if (previousBlock.getIndex() + 1 != newBlock.getIndex()) {
                System.out.println("Index is not matched in new block");
                return false;
            }

            if (newBlock.getPreviousHash() == null  ||
                    !newBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Block's previous hash " + newBlock.getPreviousHash());
                System.out.println("Previous block hash " + previousBlock.getHash());
                System.out.println("Previous hash is not matched in new block");
                return false;
            }

            if (newBlock.getHash() == null  ||
                    !Block.calculateHash(newBlock).equals(newBlock.getHash())) {
                System.out.println("New Hash is not matched in new block");
                System.out.println("New block's hash "+newBlock.getHash());
                System.out.println("Calculated hash "+Block.calculateHash(newBlock));
                return false;
            }

            return true;
        }

        return false;
    }

    public boolean isBlockChainValid() {
//        if (!isFirstBlockValid()) {
//            System.out.println("First block is not valid");
//            return false;
//        }

        for (int i = 1; i < blocks.size(); i++) {
            Block currentBlock = blocks.get(i);
            Block previousBlock = blocks.get(i - 1);

            if (!isValidNewBlock(currentBlock, previousBlock)) {
                blocks.remove(currentBlock);
                return false;
            }
        }

        return true;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Block block : blocks) {
            builder.append(block).append("\n");
        }

        return builder.toString();
    }
}
