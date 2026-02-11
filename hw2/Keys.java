import java.math.BigInteger;

public class Keys {

    public static class Key {

        public final BigInteger n;

        public Key(BigInteger n) {
            this.n = n;
        }
    }

    
    public static class PrivateKey extends Key {
        public final BigInteger d;
        
        public PrivateKey(BigInteger d, BigInteger n) {
            super(n);
            this.d = d;
        }
    }
    
    public static class PublicKey extends Key {
        public final BigInteger e;
        
        public PublicKey(BigInteger e, BigInteger n) {
            super(n);
            this.e = e;
        }
    }
}
