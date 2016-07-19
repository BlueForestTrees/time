package time.messaging.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import time.messaging.Messager;
import time.messaging.Queue;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MessageConsole {

    private static final Logger LOGGER = LogManager.getLogger(MessageConsole.class);

    private Messager messager;
    private boolean finished = false;

    @Inject
    public MessageConsole(final Messager messager){
        this.messager = messager;
    }

    public void on() throws IOException {
        System.out.println(Arrays.asList(Queue.values()));
        while(!finished) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("MessageConsole > ");
            final String command = br.readLine();
            handleCommand(command);
        }
    }

    private void handleCommand(final String command) {
        try {
            final Queue queue = Queue.valueOf(command);
            messager.signal(queue);
        }catch(IllegalArgumentException e){
            System.out.println("unkwown command: " + command);
            System.out.println(Arrays.asList(Queue.values()));
        } catch (IOException e) {
            System.out.println("IOException while signaling " + command + " " + e.getMessage());
        }
    }
}
