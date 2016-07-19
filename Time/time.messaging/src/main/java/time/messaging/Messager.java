package time.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.exception.MessagerException;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Entry-point for managing messages. Rabbit based
 */
@Singleton
public class Messager {

    private static final Logger LOGGER = LogManager.getLogger(Messager.class);

    private Channel channel;
    private Connection connection;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Create a connected messager.
     * throw MessageException if problem
     */
    public Messager() {
        on();
        installShutdownHook();
    }

    private void on() {
        try {
            LOGGER.info("on");
            connection = new ConnectionFactory().newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
            throw new MessagerException("Messager boot exception", e);
        }
    }

    private void installShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("shutdown hook received");
            off();
        }));
    }

    /**
     * Close the messager.
     */
    public void off() {
        LOGGER.info("off");
        try {
            channel.close();
        } catch (IOException | TimeoutException e) {
            LOGGER.error(e);
        } catch (AlreadyClosedException e) {
            LOGGER.info("allready closed", e);
        }finally {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    public Messager when(final Queue queue, final EventListener receiver) throws IOException{
        LOGGER.info("when {}", queue);
        channel.queueDeclare(queue.name(), false, false, false, null);
        channel.basicConsume(queue.name(), true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                LOGGER.info("received {}", queue);
                receiver.signal();
            }
        });
        return this;
    }

    /**
     * Suscribe to a messaged queue
     *
     * @param queue where message come from
     * @param type  the class of the received messages
     * @param <T>   the type of the received messages
     * @return message consumer to chain the then part
     */
    public <T> Messager when(final Queue queue, final Class<T> type, final MessageListener<T> receiver) throws IOException {
        LOGGER.info("when {}({})", queue, type.getSimpleName());
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
        return this;
    }

    /**
     * Send a signal
     *
     * @param queue where to signal
     * @return this
     * @throws IOException
     */
    public Messager signal(final Queue queue) throws IOException {
        LOGGER.info("sending signal {}", queue);
        channel.basicPublish("", queue.name(), null, null);
        return this;
    }

    /**
     * Send a message
     *
     * @param queue   where to send
     * @param message what to send
     * @param <T>     the type of what is sent
     * @return this
     * @throws IOException
     */
    public <T> Messager send(final Queue queue, T message) throws IOException {
        LOGGER.info("sending message {} {}", queue, message);
        final String messageString = mapper.writeValueAsString(message);
        channel.basicPublish("", queue.name(), null, messageString.getBytes());
        return this;
    }

}
