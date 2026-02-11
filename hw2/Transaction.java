import java.io.*;
import java.util.*;

public class Transaction {

    public static class Input {
        public final byte[]    prevTxHash;
        public final int       prevOutIndex;
        public       byte[]    signature;

        public Input(byte[] prevTxHash, int prevOutIndex) {
            this.prevTxHash   = prevTxHash;
            this.prevOutIndex = prevOutIndex;
        }

        public byte[] serialize() {
            return null;
        }
    }

    public static class Output {
        public final double    value;
        public final Keys.PublicKey recipient;

        public Output(double value, Keys.PublicKey recipient) {
            this.value     = value;
            this.recipient = recipient;
        }
    }

    private final List<Input>  inputs  = new ArrayList<>();
    private final List<Output> outputs = new ArrayList<>();
    private byte[] hash;

    public void addInput(byte[] prevTxHash, int prevOutIndex) {
        return;
    }

    public void addOutput(double value, Keys.PublicKey recipient) {
        return;
    }

    public byte[] getInputDataToSign(int inputIndex) throws IOException {
       return null;
    }

    public void signInput(int inputIndex, byte[] signature) {
        return;
    }

    public void computeHash() throws Exception {
        return;
    }
    
    public byte[] getHash() {
        return null;
    }

}
