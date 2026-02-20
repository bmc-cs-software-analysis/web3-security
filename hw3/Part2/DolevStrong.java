import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DolevStrong {
    public static final int DEFAULT = 0;

    // ---------------------------------------------------------------
    // Validity: if the general is honest, every honest party outputs
    //           exactly the general's input value v.
    // ---------------------------------------------------------------
    public static boolean validity(int v, List<DSParty> parties) {
        // TODO
        return false;
    }

    // ---------------------------------------------------------------
    // Agreement: all honest parties output the same value.
    // ---------------------------------------------------------------
    public static boolean agreement(List<DSParty> parties) {
        // TODO
        return true;
    }

    // ---------------------------------------------------------------
    // Protocol - returns true iff validity and agreement are satisfied
    // ---------------------------------------------------------------
    public static boolean protocol(List<DSParty> parties, int t, Map<Integer, Keys.PublicKey> PKI) {
        // TODO:
        // Round 1: leader sends its signed message to all other parties
        // Rounds 2 through t+1: all non-leader parties call relay()
        // Decision round.
        return false;
    }

    public static void main(String[] args) {
        // Test your protocol under various settings of n and t.
        // Run many trials per setting (dishonest behavior is non-deterministic).
        // Record your results in the README table.
    }
}
