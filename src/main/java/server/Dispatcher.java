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
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws IOException {
        //TODO: MESSAGE#Kurt#Hello Peter
        String[] messageArray = message.split(",");
        String clientName = messageArray[0];

        String[] messageArray2 = message.split("#");
        String clientMessage = messageArray2[2];

        message = "MESSAGE#" + clientName + "#" + clientMessage;

        if (activeClients.containsKey(clientName)) {
            new PrintWriter(activeClients.get(clientName).getOutputStream()).println(message);
        }
    }

}
