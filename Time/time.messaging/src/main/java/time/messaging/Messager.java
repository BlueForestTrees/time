package time.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Singleton
public class Messager {

    private static final Logger LOGGER = LogManager.getLogger(Messager.class);

    private Channel channel;
    private Connection connection;
    private final ObjectMapper mapper = new ObjectMapper();

    public Messager(){
        try {
            on();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
            throw new RuntimeException("Messager boot exception", e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("shutdown hook received");
            off();
        }));
    }

    public void on() throws IOException, TimeoutException {
        LOGGER.info("on");
        connection = new ConnectionFactory().newConnection();
        channel = connection.createChannel();
    }

    public void off() {
        LOGGER.info("off");
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
        } catch (AlreadyClosedException e) {
            LOGGER.info("allready closed");
        }
        try {
            connection.close();
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public QueueConsumer when(final Queue queue) {
        return new QueueConsumer(queue);
    }

    public <T> TypedQueueConsumer<T> when(final Queue queue, final Class<T> type) {
        return new TypedQueueConsumer<>(queue, type);
    }

    public Messager signal(final Queue queue) throws IOException {
        LOGGER.info("sending signal {}", queue);
        channel.basicPublish("", queue.name(), null, null);
        return this;
    }

    public <T> Messager signal(final Queue queue, T message) throws IOException {
        LOGGER.info("sending message " + queue + " " + message);
        final String messageString = mapper.writeValueAsString(message);
        channel.basicPublish("", queue.name(), null, messageString.getBytes());
        return this;
    }

    public class QueueConsumer {
        private final Queue queue;

        private QueueConsumer(final Queue queue) {
            this.queue = queue;
        }

        public Messager then(final Listener receiver) throws IOException {
            LOGGER.info("listen {}", queue);
            channel.queueDeclare(queue.name(), false, false, false, null);
            channel.basicConsume(queue.name(), true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    LOGGER.info("received message {}", queue);
                    receiver.signal();
                }
            });
            return Messager.this;
        }
    }

    public class TypedQueueConsumer<T> {
        private final Queue queue;
        private final Class<T> type;

        private TypedQueueConsumer(final Queue queue, final Class<T> type) {
            this.queue = queue;
            this.type = type;
        }

        public void then(final TypedListener<T> receiver) throws IOException {
            LOGGER.info("listen {}({})", queue, type.getSimpleName());
            channel.queueDeclare(queue.name(), false, false, false, null);
            channel.basicConsume(queue.name(), true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    LOGGER.info("received {}", queue);
                    final String messageString = new String(body, "UTF-8");
                    final T signal = mapper.readValue(messageString, type);
                    receiver.signal(signal);
                }
            });

        }
    }

}
