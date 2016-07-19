package time.messaging.exception;

public class MessagerException extends RuntimeException {
    public MessagerException(String s, Exception e) {
        super(s,e);
    }
}
