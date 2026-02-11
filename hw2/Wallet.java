import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Wallet {

    private final Keys.PrivateKey privateKey;
    private final Keys.PublicKey  publicKey;

    public Wallet(int keySize) {
        privateKey = null;
        publicKey = null;
    }

    public Keys.PublicKey getPublicKey() {
        return null;
    }

    public Keys.Key[] generateKeys(int keySize) {
        return null; 
    }

    public byte[] sign(byte[] message) {
        return null; 
    }

    public static boolean verify(Keys.PublicKey pk, byte[] message, byte[] signature) {
        return false;    
    }

}
