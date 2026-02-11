import java.util.*;

/**
 * CentralCoin - a centralized cryptocurrency using blockchain + merkle trees.
 *
 * Architecture:
 * - Blockchain: append-only log with hash pointers (Part 3)
 * - Each Block contains: merkle tree of transactions (Part 4)
 * - UTXOPool: derived state - rebuilt from blockchain (demonstrates concept)
 *
 * Workflow:
 * 1. Transactions accumulate in pending pool
 * 2. mineBlock() packages them into a block with merkle tree
 * 3. Block is appended to blockchain
 * 4. UTXO pool is updated to reflect new state
 */
public class CentralCoin {

    private final Wallet    theMint;
    private final Blockchain blockchain;
    private final UTXOPool  utxoPool;
    private final List<Transaction> pendingTxs;

    public CentralCoin()  {
        theMint = null;    
        blockchain = null;
        utxoPool = null;
        pendingTxs = null;
    }

    public Transaction createCoins(double amount, Keys.PublicKey recipient) throws Exception {
        
        return null;
    }

    
    public boolean processTransaction(Transaction tx) throws Exception {
        return false; 
    }

    
    public Block mineBlock()  {
        return null;
        
    }

    public void rebuildUTXOPool() {
        
    }

    public Blockchain getBlockchain() { return blockchain; }
    public UTXOPool getUTXOPool() { return utxoPool; }
}
