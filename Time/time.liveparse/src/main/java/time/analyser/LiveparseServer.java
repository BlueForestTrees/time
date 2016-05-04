package time.analyser;

import com.google.inject.Inject;
import com.google.inject.name.Named;


public class LiveparseServer {

    private final int port;

    @Inject
    public LiveparseServer(@Named("port") int port){
        this.port = port;
    }

    public void start() throws Exception {

    }
}
