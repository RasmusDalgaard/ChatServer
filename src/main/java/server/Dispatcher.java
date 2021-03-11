package server;

import exceptions.NoSuchClientException;

import javax.print.attribute.standard.PrinterStateReasons;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.PrimitiveIterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcher extends Thread {

    ConcurrentMap<String, Socket> activeClients;
    BlockingQueue<String> allMessages;
    BlockingQueue<String> clientsToClose;

    public Dispatcher(ConcurrentMap<String, Socket> activeClients, BlockingQueue<String> allMessages) {
        this.activeClients = activeClients;
        this.allMessages = allMessages;
    }

    public Dispatcher(ConcurrentMap<String, Socket> activeClients, BlockingQueue<String> allMessages, BlockingQueue<String> clientsToClose) {
        this.activeClients = activeClients;
        this.allMessages = allMessages;
        this.clientsToClose = clientsToClose;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = allMessages.take();
                sendMessage(message);
            } catch (InterruptedException | NoSuchClientException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) throws NoSuchClientException {
        Set<String> nameSet = activeClients.keySet();
        StringBuilder sb = new StringBuilder();

        // command#clientName,receiver#message
        String[] messageArray = message.split("#");

        //The command
        String token = messageArray[0];

        switch (token) {
            case "SEND":
                //clientName,receivers
                String users = messageArray[1];
                String[] userArray = users.split(",");
                //clientname
                String sender = userArray[0];
                //The message
                String userMessage = messageArray[2];
                sb.append("MESSAGE#" + sender + "#" + userMessage);
                if (users.contains("*")) {
                    for (String name : nameSet) {
                        findPrintWriter(name).println(sb.toString());
                    }
                } else {
                    for (int i = 1; i < userArray.length; i++) {
                        findPrintWriter(userArray[i]).println(sb.toString());
                    }
                }
                break;

            case "ONLINE":
                sb.append("ONLINE#");
                for (String name : nameSet) {
                    sb.append(name + ",");
                }
                for (String name : nameSet) {
                    findPrintWriter(name).println(sb.toString());
                }
                break;

            case "CLOSE":
                String clientName = messageArray[1];
                sb.append(token + "#" + messageArray[2]);
                findPrintWriter(clientName).println(sb.toString());
                clientsToClose.add(clientName);
                break;
        }
    }

    public PrintWriter findPrintWriter(String clientName) throws NoSuchClientException {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(activeClients.get(clientName).getOutputStream(), true);
        } catch (IOException e) {
            throw new NoSuchClientException("There is no client named: " + clientName);
        } catch (NullPointerException e) {
            throw new NoSuchClientException("There is no client named: " + clientName);
        }
        return pw;
    }
}
