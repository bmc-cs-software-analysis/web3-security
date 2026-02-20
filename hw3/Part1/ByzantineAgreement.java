import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ByzantineAgreement {

    public static int     DEFAULT = 0;

    // ---------------------------------------------------------------
    // Validity: if the general is honest, every honest party must
    //           output exactly the general's input value v.
    // ---------------------------------------------------------------
    public static boolean validity(int v, List<Party> parties) {
       //TODO: 
       return false;
    }

    // ---------------------------------------------------------------
    // Agreement: all honest parties output the same value.
    // ---------------------------------------------------------------
    public static boolean agreement(List<Party> parties) {
        //TODO:
        return true;
    }

    // ---------------------------------------------------------------
    // Protocol - returns true iff validity and agreement are satisfied
    // ---------------------------------------------------------------
    public static boolean protocol(List<Party> parties, Map<Integer, Keys.PublicKey> PKI) {
        //TODO:
        //  Round 1: The general sends the signed bit to all other parties.
        //           The cases of the general being honest / dishonest should be handled in send.
        //  Round 2: Relay (send) any messages recieved in round 1 to all other parties.
        //           honest / dishonest behavior of non-general parties should also be handled in send.
        //  Decision Round.
        return false;
    }

    public static void main(String[] args) {
        //Test your protocol under various settings of n and t.
        //Test if validity and agreement are satisfied.
        //Since the dishonest send is non-deterministic, run your protocol **many** times under each setting.
        //If you ever hit a case where validity and agreement are not met, you know that it is not satisfied for the protocol.
        //However, seeing 1 run for which they ARE satisfied does not mean the protocol satisfies BG. 
        //Make sure to run a sufficient number of trials
    }

}
