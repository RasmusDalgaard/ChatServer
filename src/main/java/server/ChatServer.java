package server;


import client.ClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatServer {

    BlockingQueue<String> allMessages = new ArrayBlockingQueue<>(200);

    BlockingQueue<String> clientsToclose = new ArrayBlockingQueue<>(200);
    ConcurrentMap<String, Socket> activeClients = new ConcurrentHashMap<>();

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        int port = 8088;
            try {
                ChatServer chatServer = new ChatServer();
                chatServer.runServer(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void runServer(int port) throws IOException {
        Dispatcher dispatcher = new Dispatcher(activeClients, allMessages, clientsToclose);
        dispatcher.start();

        CleanUp cleanUp = new CleanUp(clientsToclose, activeClients);
        cleanUp.start();

        int counter = 0;
        int limit = 4;
        ServerSocket serverSocket = new ServerSocket(port);
        while (counter < limit) {
            counter++;
            System.out.println("Waiting for client");
            Socket client = serverSocket.accept();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            //CONNECT#Clientname
            String username = bufferedReader.readLine();
            String[] usernameArray = username.split("#");
            activeClients.put(usernameArray[1], client);

            ClientHandler clientHandler = new ClientHandler(client, usernameArray[1], allMessages);
            clientHandler.start();
            allMessages.add("ONLINE#");
        }
    }
}
