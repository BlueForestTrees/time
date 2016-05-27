package time.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Messager {

    private static final Logger LOGGER = LogManager.getLogger(Messager.class);

    Channel channel;
    Connection connection;
    final ObjectMapper mapper;

    public Messager() throws IOException, TimeoutException {
        LOGGER.info("Messager()");
        mapper = new ObjectMapper();
        on();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> off()));
    }

    public void on() throws IOException, TimeoutException {
        LOGGER.info("ON");
        connection = new ConnectionFactory().newConnection();
        channel = connection.createChannel();
    }

    public void off() {
        LOGGER.info("OFF");
        try {
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
        }
    }

    public QueueConsumer when(final Queue queue) {
        return new QueueConsumer(queue);
    }

    public Messager signal(final Queue queue) throws IOException {
        LOGGER.info("sending signal {}", queue);
        channel.basicPublish("", queue.name(), null, null);
        return this;
    }

    public class QueueConsumer {
        private final Queue queue;
        private QueueConsumer(final Queue queue){
            this.queue = queue;
        }
        public Messager then(final Listener receiver) throws IOException {
            LOGGER.info(receiver + "{} simply listen {}", receiver, queue);
            channel.queueDeclare(queue.name(), false, false, false, null);
            channel.basicConsume(queue.name(), true, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    LOGGER.info("received {}", queue);
                    receiver.signal();
                }
            });
            return Messager.this;
        }
    }

/*
    //TYPED

        //TYPED
    public <T> Sender use(final String queue, final Class<T> type){
        LOGGER.info("creating sender queue " + queue + " use type " + type.getSimpleName());
        return new TypedSender(queue, type);
    }

    public <T> Messager addReceiver(final Consumer<T> receiver, final Class<T> type) throws IOException {
        LOGGER.info(receiver + " listen " + receiver.getQueue() + "("+type.getSimpleName()+")");

        final Queue receiverQueue = receiver.getQueue();
        final com.rabbitmq.client.Consumer consumer = new TypedConsumer(receiver, type);
        channel.queueDeclare(receiverQueue.name(), false, false, false, null);
        channel.basicConsume(receiverQueue.name(), true, consumer);

        return this;
    }

    private class TypedSender<T> implements Sender<T> {
        private final Class<T> type;
        public String queue;
        public TypedSender(final String queue, final Class<T> type){
            this.queue = queue;
            this.type = type;
        }
        @Override
        public void send(T signal) throws IOException {
            LOGGER.info("sending signal to queue " + queue);
            final String messageString = mapper.writeValueAsString(signal);
            channel.basicPublish("", queue, null, messageString.getBytes());
        }
    }

    private class TypedConsumer<T> extends DefaultConsumer {
        private final Consumer<T> receiver;
        private final Class<T> type;

        public TypedConsumer(final Consumer<T> receiver, final Class<T> type) {
            super(channel);
            this.receiver = receiver;
            this.type = type;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            final String messageString = new String(body, "UTF-8");

            final T signal = mapper.readValue(messageString, type);

            receiver.signal(signal);
        }
    }*/
}
