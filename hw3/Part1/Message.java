public class Message {
    public final int value;
    public final byte[] sig;

    public Message(int value, byte[] sig) {
        this.value = value;
        this.sig   = sig;
    }
}
