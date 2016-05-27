package time.messaging.console;

import time.messaging.Queue;

import java.util.List;

public class QueueLinks {

    public List<QueueLink> getQueueLinks() {
        return queueLinks;
    }

    private List<QueueLink> queueLinks;

    public static class QueueLink {
        private Queue from;
        private Queue to;
        public Queue getFrom() {
            return from;
        }
        public Queue getTo() {
            return to;
        }
    }
}
