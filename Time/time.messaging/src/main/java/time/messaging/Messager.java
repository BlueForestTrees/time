package time.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Messager {

    private static final Logger LOGGER = LogManager.getLogger(Messager.class);

    final Channel channel;
    final Connection connection;
    final ObjectMapper mapper;

    public Messager() throws IOException, TimeoutException {
        LOGGER.info("Messager()");
        connection = new ConnectionFactory().newConnection();
        channel = connection.createChannel();
        mapper = new ObjectMapper();
    }

    public void stop() throws IOException, TimeoutException {
        LOGGER.info("Messager.stop()");
        channel.close();
        connection.close();
    }

    //SIMPLE
    public void addReceiver(final time.messaging.SimpleConsumer timeConsumer) throws IOException {
        LOGGER.info(timeConsumer + " simply listen " + timeConsumer.getQueue());

        final Queue receiverQueue = timeConsumer.getQueue();
        final com.rabbitmq.client.Consumer consumer = new SimpleConsumer(timeConsumer);
        channel.queueDeclare(receiverQueue.name(), false, false, false, null);
        channel.basicConsume(receiverQueue.name(), true, consumer);
    }

    public time.messaging.SimpleSender getSender(final Queue queue){
        LOGGER.info("creating simple sender queue " + queue);
        return new SimpleSender(queue);
    }

    //TYPED
    public <T> Sender getSender(final String queue, final Class<T> type){
        LOGGER.info("creating sender queue " + queue + " with type " + type.getSimpleName());
        return new TypedSender(queue, type);
    }

    public <T> void addReceiver(final Consumer<T> timeConsumer, final Class<T> type) throws IOException {
        LOGGER.info(timeConsumer + " listen " + timeConsumer.getQueue() + "("+type.getSimpleName()+")");

        final Queue receiverQueue = timeConsumer.getQueue();
        final com.rabbitmq.client.Consumer consumer = new TypedConsumer(timeConsumer, type);
        channel.queueDeclare(receiverQueue.name(), false, false, false, null);
        channel.basicConsume(receiverQueue.name(), true, consumer);
    }



    //SIMPLE
    private class SimpleSender implements time.messaging.SimpleSender {
        public Queue queue;
        public SimpleSender(final Queue queue){
            this.queue = queue;
        }
        @Override
        public void signal() throws IOException {
            LOGGER.info("sending signal to queue " + queue);
            channel.basicPublish("", queue.name(), null, null);
        }
    }

    private class SimpleConsumer extends DefaultConsumer {
        private final time.messaging.SimpleConsumer receiver;

        public SimpleConsumer(final time.messaging.SimpleConsumer receiver) {
            super(channel);
            this.receiver = receiver;
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            receiver.message();
        }

    }


    //TYPED
    private class TypedSender<T> implements Sender<T> {
        private final Class<T> type;
        public String queue;
        public TypedSender(final String queue, final Class<T> type){
            this.queue = queue;
            this.type = type;
        }
        @Override
        public void send(T message) throws IOException {
            LOGGER.info("sending message to queue " + queue);
            final String messageString = mapper.writeValueAsString(message);
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

            final T message = mapper.readValue(messageString, type);

            receiver.message(message);
        }
    }
}
