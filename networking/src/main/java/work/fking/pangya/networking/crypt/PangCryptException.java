package work.fking.pangya.networking.crypt;

public class PangCryptException extends RuntimeException {

    public PangCryptException(String message) {
        super(message);
    }

    public PangCryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public PangCryptException(Throwable cause) {
        super(cause);
    }
}
