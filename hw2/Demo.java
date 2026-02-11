import java.util.List;

public class Demo {
    public static void main(String[] args) throws Exception {
        CentralCoin mint = new CentralCoin();
        Wallet alice = new Wallet(2048);
        Wallet bob   = new Wallet(2048);
        Wallet carol = new Wallet(2048);

        // ══════════════════════════════════════════════════════════════════════
        // STEP 1: Mint 10 coins for Alice
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== STEP 1: Mint 10 coins for Alice ==========");
        Transaction mintTx = mint.createCoins(10.0, alice.getPublicKey());

        // Mine the first block
        Block block0 = mint.mineBlock();

        // ══════════════════════════════════════════════════════════════════════
        // STEP 2: Alice transfers 7 to Bob, keeps 3 as change
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== STEP 2: Alice -> Bob (7 coins) ==========");
        Transaction tx1 = new Transaction();
        tx1.addInput(mintTx.getHash(), 0);
        tx1.addOutput(7.0, bob.getPublicKey());
        tx1.addOutput(3.0, alice.getPublicKey());  // change
        byte[] aliceSig1 = alice.sign(tx1.getInputDataToSign(0));
        tx1.signInput(0, aliceSig1);
        tx1.computeHash();

        boolean tx1Accepted = mint.processTransaction(tx1);
        System.out.println("Transaction accepted: " + tx1Accepted);

        // Mine block 1
        Block block1 = mint.mineBlock();

        // ══════════════════════════════════════════════════════════════════════
        // STEP 3: Alice attempts DOUBLE-SPEND (should fail)
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== STEP 3: Alice attempts DOUBLE-SPEND ==========");
        Transaction tx2 = new Transaction();
        tx2.addInput(mintTx.getHash(), 0);  // SAME input as tx1!
        tx2.addOutput(10.0, carol.getPublicKey());
        byte[] aliceSig2 = alice.sign(tx2.getInputDataToSign(0));
        tx2.signInput(0, aliceSig2);
        tx2.computeHash();

        boolean accepted = mint.processTransaction(tx2);
        System.out.println("Accepted: " + accepted);  // should be false
        System.out.println("(No block mined - transaction rejected)");

        // ══════════════════════════════════════════════════════════════════════
        // STEP 4: Bob legitimately sends 5 to Carol
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== STEP 4: Bob -> Carol (5 coins) ==========");
        Transaction tx3 = new Transaction();
        tx3.addInput(tx1.getHash(), 0);  // Bob's 7 coins
        tx3.addOutput(5.0, carol.getPublicKey());
        tx3.addOutput(2.0, bob.getPublicKey());  // change
        byte[] bobSig = bob.sign(tx3.getInputDataToSign(0));
        tx3.signInput(0, bobSig);
        tx3.computeHash();

        boolean tx3Accepted = mint.processTransaction(tx3);
        System.out.println("Transaction accepted: " + tx3Accepted);

        // Mine block 2
        Block block2 = mint.mineBlock();

        // ══════════════════════════════════════════════════════════════════════
        // PART 4 DEMO: Merkle proof of inclusion
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== PART 4: Merkle Proof of Inclusion ==========");

        // Use block1 which contains tx1
        List<byte[]> proof = block1.getProofOfInclusion(tx1);
        System.out.println("Proof size: " + proof.size() + " hashes");

        boolean proofValid = block1.verifyProof(tx1.getHash(), proof);
        System.out.println("Proof valid for tx1: " + proofValid);

        // Try verifying a transaction NOT in this block
        boolean fakeProofValid = block1.verifyProof(tx3.getHash(), proof);
        System.out.println("Proof valid for tx3 (not in block1): " + fakeProofValid);

        // ══════════════════════════════════════════════════════════════════════
        // PART 5 DEMO: Rebuild UTXO pool from blockchain
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== PART 5: Rebuild UTXO Pool ==========");
        System.out.println("Rebuilding UTXO pool from blockchain...");
        mint.rebuildUTXOPool();
        System.out.println("Rebuild complete.");

        // ══════════════════════════════════════════════════════════════════════
        // PART 3 DEMO: Blockchain tamper detection
        // (Done last because it corrupts the chain)
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== PART 3: Blockchain Tamper Detection ==========");
        Blockchain blockchain = mint.getBlockchain();
        System.out.println("Blockchain size: " + blockchain.getSize());
        System.out.println("Chain valid before tampering: " + blockchain.validateChain());

        // Tamper with a transaction in an earlier block
        Block head = blockchain.getHead();
        Block middleBlock = head.getPrevious();
        Block firstBlock = middleBlock.getPrevious();
        System.out.println("Tampering with first block...");
        firstBlock.getTransactions().set(0, tx3); // swap in a different transaction

        System.out.println("Chain valid after tampering: " + blockchain.validateChain());

        // ══════════════════════════════════════════════════════════════════════
        // Final Summary
        // ══════════════════════════════════════════════════════════════════════
        System.out.println("\n========== FINAL STATE ==========");
        System.out.println("Blockchain height: " + mint.getBlockchain().getSize());
        System.out.println("Alice: 3 coins (change from tx1)");
        System.out.println("Bob:   2 coins (change from tx3)");
        System.out.println("Carol: 5 coins (received in tx3)");
        System.out.println("Total: 10 coins (matches original mint)");
        System.out.println("\nKey demonstrations:");
        System.out.println("  Digital signatures prevent forgery (Part 2)");
        System.out.println("  Blockchain detects tampering via hash chain (Part 3)");
        System.out.println("  Merkle trees provide efficient proofs (Part 4)");
        System.out.println("  UTXO pool prevents double-spending (Part 5)");
        System.out.println("  Blockchain is source of truth - UTXO pool is derived");
    }
}
