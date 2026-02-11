import java.util.*;

/**
 * Blockchain - An append-only, tamper-resistant log.
 *
 * A linked list of blocks connected by hash pointers.
 * Each block contains a list of transactions.
 * Any modification to a past block invalidates all subsequent hash pointers.
 */
public class Blockchain {

    private Block head;
    private int size;

    /**
     * Creates an empty blockchain.
     */
    public Blockchain() {
        // TODO
    }

    /**
     * Creates a new block from the given transactions and appends it to the chain.
     *
     * The new block's previous pointer should reference the current head block
     * (or null if this is the first block in the chain).
     *
     * @param transactions the list of transactions to include in the new block
     */
    public void append(List<Transaction> transactions) {
        // TODO
    }

    /**
     * Returns the most recent block in the chain, or null if the chain is empty.
     *
     * @return the head block
     */
    public Block getHead() {
        // TODO
        return null;
    }

    /**
     * Returns the number of blocks in the chain.
     *
     * @return the chain length
     */
    public int getSize() {
        // TODO
        return 0;
    }

    /**
     * Validates the integrity of the entire chain.
     *
     * @return true if the entire chain is intact, false if any block
     *         has been tampered with
     */
    public boolean validateChain() {
        // TODO
        return false;
    }
}
