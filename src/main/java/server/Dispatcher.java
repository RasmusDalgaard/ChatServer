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
        //TODO: MESSAGE#Peter#Hello Hans
        String[] messageArray = message.split("#");
        String users = messageArray[1];
        String userMessage = messageArray[2];

        String[] userArray = users.split(",");
        String sender = userArray[0];
        String receiver = userArray[1];

        String messageToSend = "MESSAGE#" + sender + "#" + userMessage;

        for (int i = 1; i < userArray.length; i++) {
            findPrintWriter(userArray[i]).println(messageToSend);
        }
    }

    public PrintWriter findPrintWriter(String clientName) throws IOException {
        PrintWriter pw = new PrintWriter(activeClients.get(clientName).getOutputStream(),true);
        return pw;
    }

}
