import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Party {

    public final boolean  isLeader;
    public final boolean  isHonest;

    private final Wallet  wallet;      
    public  final List<Message> msgs;  // V_i
    public  Integer output;            // set during decision phase (decide)

    public Party(boolean isLeader, boolean isHonest) {
        this.isLeader = isLeader;
        this.isHonest = isHonest;
        this.wallet   = new Wallet(512); 
        this.msgs     = new ArrayList<>();
        this.output   = null;
    }

    public Keys.PublicKey getPublicKey() {
        return wallet.getPublicKey();
    }

    public Message sign(int v) {
        //TODO: sign the message using the party's wallet 
        return null;
    }

    public void send(Party receiver, Message msg, Map<Integer, Keys.PublicKey> PKI) {
        if (!isHonest) {
            // TODO: Implement a dishonest send protocol.
            //       Think adversarially... a faulty party can deviate from the protocol in any way
            return;
        }

        receiver.receive(msg, PKI);
    }

    
    public void receive(Message msg, Map<Integer, Keys.PublicKey> PKI) {
        //TODO: validate that the message was signed by the general and add it to V_i if so
    }

    public void decide() {
        if (isHonest) {
            // TODO
        } else {
            // TODO
        }
    }
}
