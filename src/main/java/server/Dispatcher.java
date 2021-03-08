package server;

import javax.print.attribute.standard.PrinterStateReasons;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PrimitiveIterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcher extends Thread {

    ConcurrentMap<String, Socket> activeClients;
    BlockingQueue<String> allMessages;


    public Dispatcher(ConcurrentMap<String, Socket> activeClients, BlockingQueue<String> allMessages) {
        this.activeClients = activeClients;
        this.allMessages = allMessages;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = allMessages.take();
                sendMessage(message);
                // TODO: SEND#Kurt,Peter#Hello Peter
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        //TODO: MESSAGE#Kurt#Hello Peter
        String clientName = "Peter";
        if (activeClients.containsKey(clientName)) {
            new PrintWriter(activeClients.get(clientName).getOutputStream()).println("Hej peter");
        }
    }

}
