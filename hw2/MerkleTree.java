import java.security.*;
import java.util.*;

public class MerkleTree {

    /** A node in the merkle tree (internal node or leaf). */
    private static class Node {
        byte[] hash;
        Node left, right;
        boolean isLeaf;

        Node(byte[] hash, boolean isLeaf) {
            this.hash = hash;
            this.isLeaf = isLeaf;
        }
    }

    private final Node root;
    private final List<byte[]> leafHashes;  // original transaction hashes

    
    public MerkleTree(List<byte[]> txHashes)  {
        root = null;
        leafHashes = null;
    }

    public byte[] getRootHash() {
        return null;
    }

    /**
     * Generates a proof that txHash is in the tree.
     * Returns the list of sibling hashes needed to reconstruct the root.
     * Returns null if txHash is not in the tree.
     */
    public List<byte[]> getProofOfInclusion(byte[] txHash) {
        return null;
    }

    /**
     * Verifies that txHash is in a tree with the given rootHash,
     * using the provided proof (list of sibling hashes).
     */
    public static boolean verifyProof(byte[] txHash, List<byte[]> proof, byte[] rootHash) {
        return false;
    }

}
