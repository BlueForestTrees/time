package time.messaging.console;

import time.conf.ConfManager;
import time.conf.Confs;
import time.messaging.Messager;
import time.messaging.Queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MessagingConsole {

    public static void main(String[] args) throws IOException, TimeoutException {

        final QueueLinks queueLinks = new ConfManager().get(Confs.LINKS, QueueLinks.class);


        final Messager messager = new Messager();
        messager.when(Queue.WIKI_CRAWL_END).signal(Queue.MERGE);
        messager.when(Queue.MERGED).signal(Queue.WIKI_WEB_REFRESH);
    }

}
