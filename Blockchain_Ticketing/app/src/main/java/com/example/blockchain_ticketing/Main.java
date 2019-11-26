package com.example.blockchain_ticketing;

public class Main {

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain(4);
        blockchain.addBlock(blockchain.newBlock("Tout sur le Bitcoin"));
        blockchain.addBlock(blockchain.newBlock("Sylvain Saurel"));
        blockchain.addBlock(blockchain.newBlock("https://www.toutsurlebitcoin.fr"));

        System.out.println("Blockchain valid ? " + blockchain.isBlockChainValid());
        System.out.println(blockchain);

        // add an invalid block to corrupt Blockchain
        blockchain.addBlock(new Block(15, System.currentTimeMillis(), "aaaabbb", "Block invalid"));

        System.out.println("Blockchain valid ? " + blockchain.isBlockChainValid());
    }
}
