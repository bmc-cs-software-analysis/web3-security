import java.util.ArrayList;
import java.util.List;

public class DSMessage {
    public final int value;
    private final List<Signature> signatures;

    
    public DSMessage(int value) {
        this.value      = value;
        this.signatures = new ArrayList<>();
    }

    public static class Signature {

        public final int    partyId;  // which party signed
        public final byte[] bytes;    

        public Signature(int partyId, byte[] bytes) {
            this.partyId = partyId;
            this.bytes   = bytes;
        }

    }

    private DSMessage(int value, List<Signature> sigs) {
        this.value      = value;
        this.signatures = new ArrayList<>(sigs);
    }

    public List<Signature> getSignatures() {
        return new ArrayList<>(signatures);
    }

    public int chainLength() {
        return signatures.size();
    }

    public DSMessage addSig(Signature sig) {
        DSMessage copy = new DSMessage(this.value, this.signatures);
        copy.signatures.add(sig);
        return copy;
    }
}
