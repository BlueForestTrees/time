package time.merge;

import org.junit.Test;
import time.merger.Merge;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TestMerge {
    @Test
    public void sendMerge() throws IOException, TimeoutException {

        Merge merge = new Merge();
        merge.setMergedIndexesDir("destDir you you");
        merge.setMergeableIndexesDir("srcDir now now");

        new Messager().getSender(Queue.MERGE.name(), Merge.class).send(merge);
    }
}
