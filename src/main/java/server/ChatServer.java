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
    ConcurrentMap<String, Socket> clientNames = new ConcurrentHashMap<>();

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        String ip = "localhost";
        int port = 8088;
        try {
            if (args.length == 2) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
            } else {
                throw new IllegalArgumentException("Server not provided with the right arguments");
            }
            ChatServer chatServer = new ChatServer();
            chatServer.runServer(port);
        } catch (NumberFormatException | IOException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }

    }

    public void runServer(int port) throws IOException {
        Dispatcher dispatcher = new Dispatcher(clientNames, allMessages);
        dispatcher.start();
        int counter = 0;
        int limit = 3;
        ServerSocket serverSocket = new ServerSocket(port);
        while (counter < limit) {
            counter++;
            System.out.println("Waiting for client");
            Socket client = serverSocket.accept();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            // TODO: CONNECT#Clientname
            String name = bufferedReader.readLine();
            String[] nameArray = name.split("#");
            clientNames.put(nameArray[1], client);

            ClientHandler clientHandler = new ClientHandler(client, nameArray[1], allMessages);
            clientHandler.start();


        }
    }


}
