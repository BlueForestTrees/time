package time.messaging;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.TimeoutException;

public class MessagerTest {

    @Test
    public void testOnOff() throws IOException, TimeoutException {
        final Messager messager = new Messager();



        //ForkJoinPool.commonPool().submit(() -> messager.off());
    }
}
