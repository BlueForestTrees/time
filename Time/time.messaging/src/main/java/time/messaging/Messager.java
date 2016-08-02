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
 * Entry-point for managing messages, Rabbit based
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

    /**
     * Rabbit connection
     */
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
    /**
     * Install off call if stop signal received
     */
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
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.error(e);
            }
        }
    }

    /**
     * Install a receiver on a queue.
     * @param queue Queue to use
     * @param receiver will receives the signals via its functional method.
     * @return Fluent Message return
     * @throws IOException low-lev problem
     */
    public Messager when(final Queue queue, final EventListener receiver) throws IOException {
        LOGGER.info("when {}", queue);
        channel.queueDeclare(queue.name(), false, false, false, null);
        channel.basicConsume(queue.name(), true, toConsumer(queue, receiver));
        return this;
    }

    /**
     * Dress up a messaging receiver onto a rabbit consumer
     */
    private DefaultConsumer toConsumer(final Queue queue, final EventListener receiver) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                LOGGER.info("received {}", queue);
                receiver.signal();
            }
        };
    }

    /**
     * Send a signal
     *
     * @param queue where to signal
     * @return this
     * @throws IOException low-lev error
     */
    public Messager signal(final Queue queue) throws IOException {
        LOGGER.info("sending signal {}", queue);
        channel.basicPublish("", queue.name(), null, null);
        return this;
    }


    /**
     * Suscribe to a messaged queue
     *
     * @param queue    where message come from
     * @param type     the class of the received messages
     * @param <T>      the type of the received messages
     * @param receiver what happens when a message is coming.
     * @return message consumer to chain the then part
     * @throws IOException low-lev error
     */
    public <T> Messager when(final Queue queue, final Class<T> type, final MessageListener<T> receiver) throws IOException {
        LOGGER.info("when {}({})", queue, type.getSimpleName());
        channel.queueDeclare(queue.name(), false, false, false, null);
        channel.basicConsume(queue.name(), true, toReceiver(queue, type, receiver));
        return this;
    }

    private <T> DefaultConsumer toReceiver(final Queue queue, final Class<T> type, final MessageListener<T> receiver) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                LOGGER.info("received {}", queue);
                final String messageString = new String(body, "UTF-8");
                final T signal = mapper.readValue(messageString, type);
                receiver.signal(signal);
            }
        };
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
