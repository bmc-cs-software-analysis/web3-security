import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class HashPointer {
    private Block cur;        
    private Block prev;
    private String hash;        

    public HashPointer(Block cur, Block prev) {
        this.cur = cur;
        this.prev = prev;
        this.hash = calculateHash();
    }

    /**
     * Calculate the SHA-256 hash of this block's contents
     * 
     * The hash is computed from:
     *   - The document content
     *   - The timestamp
     *   - The hash of the previous block (from the hash pointer)
     * 
     * This creates a chain: changing any block changes all subsequent hashes
     * 
     * @return The SHA-256 hash as a hexadecimal string
     */
    public String calculateHash() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // TODO: Change this line to include all current block data
            String input = "";

            if (prev != null) {
                input += prev.getHashPointer().calculateHash();
            }

            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public Block dereference() {
        return prev;
    }

    public String getHash() {
        return hash;
    }

}
