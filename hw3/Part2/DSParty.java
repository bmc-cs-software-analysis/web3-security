import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DSParty {
    public final int     partyId;   // 1-indexed; party 1 is the leader
    public final boolean isLeader;
    public final boolean isHonest;
    private final Wallet wallet;

    public final List<DSMessage> msgs;
    public Integer output;

    public DSParty(int partyId, boolean isLeader, boolean isHonest) {
        this.partyId  = partyId;
        this.isLeader = isLeader;
        this.isHonest = isHonest;
        this.wallet   = new Wallet(512);
        this.msgs     = new ArrayList<>();
        this.output   = null;
    }

    public Keys.PublicKey getPublicKey() {
        return wallet.getPublicKey();
    }

    public DSMessage.Signature sign(DSMessage msg) {
        //TODO: call sign on the concatenation of the value and all existing signatures on the chain. 
        return null;
    }

    public void send(DSParty receiver, DSMessage msg, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
        if (!isHonest) {
            // TODO: implement dishonest send behavior
            return;
        }
        receiver.receive(msg, roundNum, PKI);
    }

    public void receive(DSMessage msg, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
        // TODO
    }

    public void relay(List<DSParty> allParties, int roundNum, Map<Integer, Keys.PublicKey> PKI) {
        if (isHonest) {
            // TODO: for each message in msgs, add your signature and send to all
        } else {
            // TODO: implement dishonest relay behavior
        }
    }

    public void decide() {
        if (isHonest) {
            // TODO
        } else {
            // TODO
        }
    }
}
