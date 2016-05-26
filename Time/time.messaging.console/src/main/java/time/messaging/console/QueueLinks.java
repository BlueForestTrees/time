package time.messaging.console;

import time.messaging.Queue;

import java.util.List;

public class QueueLinks {

    public List<QueueLink> queueLinks;

    public class QueueLink {
        private Queue from;
        private Queue to;
    }
}
