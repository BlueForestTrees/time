package time.web.exception;

public class LuceneRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 388901117757886019L;
    
    public LuceneRuntimeException(Exception e){
        super(e);
    }

}
