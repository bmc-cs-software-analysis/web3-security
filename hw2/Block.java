import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Block {
    
    public Block(List<Transaction> transactions, Block previousBlock) {

    }
    
    public List<Transaction> getTransactions() { 
        return null; 
    }
    
    public String getTimestamp() { 
        return null; 
    }
    
    public Block getPrevious() { 
        return null; 
    }

    public HashPointer getHashPointer() {
        return null;
    }

    public List<byte[]> getProofOfInclusion(Transaction tx) {
        return null;
    }

    public boolean verifyProof(byte[] txHash, List<byte[]> proof) {
        return false;
    }
}
