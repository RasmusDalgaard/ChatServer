package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class DispatcherTest {

    BlockingQueue<String> allMessages;
    ConcurrentMap<String, Socket> activeClients;
    String message;
    ServerSocket serverSocket;
    Dispatcher d;
    Socket c1;
    Socket c2;
    Thread testThread;

    @BeforeEach
    void setUp() throws IOException {

        serverSocket = new ServerSocket(8088);

        c1 = new Socket();
        c2 = new Socket();

        activeClients = new ConcurrentHashMap<>();

        activeClients.put("Hanne", c1);
        activeClients.put("Gitte", c2);
        allMessages = new ArrayBlockingQueue<>(10);
        allMessages.add(message);

        message = "SEND#Hanne#Hej Gitte!";


        d = new Dispatcher(activeClients, allMessages);
        testThread = new Thread(d);

    }

    @Test
    void sendMessage() throws IOException {


        String[] messageArray = message.split("#");
        String clientName = messageArray[1];
        String clientMessage = messageArray[2];

        message = "MESSAGE#" + clientName + "#" + clientMessage;

        //System.out.println(message);


        if (activeClients.containsKey(clientName)) {
            new PrintWriter(activeClients.get(clientName).getOutputStream()).println(message);
        }
    }
}