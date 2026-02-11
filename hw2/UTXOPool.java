public class UTXOPool {

    public void addUTXO(byte[] txHash, int outIndex, Transaction.Output output) {
        return;
    }

    public void removeUTXO(byte[] txHash, int outIndex) {
        return;
    }

    public Transaction.Output getOutput(byte[] txHash, int outIndex) {
        return null;
    }

    public boolean contains(byte[] txHash, int outIndex) {
        return false;
    }
}
