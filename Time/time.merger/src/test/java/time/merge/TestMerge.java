package time.merge;

import org.junit.Test;
import time.merger.Merge;
import time.messaging.Messager;
import time.messaging.Queues;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestMerge {
    @Test
    public void sendMerge() throws IOException, TimeoutException {

        Merge merge = new Merge();
        merge.setMergedIndexDir("destDir you you");
        merge.setMergeableIndexesDir("srcDir now now");

        new Messager().getSender(Queues.MERGE.name(), Merge.class).send(merge);
    }
}
