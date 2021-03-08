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
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChatServer {

    ConcurrentMap<String, Socket> activeClients = new ConcurrentHashMap<>();

    //Call server with arguments like this: 0.0.0.0 8088 logfile.log
    public static void main(String[] args) throws UnknownHostException {
        String ip ="localhost";
        String logFile = "log.txt";  //Do we need this
        int port = 8088;

        try {
            if (args.length == 3) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
                logFile = args[2];
            }
            else {
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
        Dispatcher dispatcher = new Dispatcher(activeClients);
        dispatcher.start();
        int counter = 0;
        int limit = 3;
        ServerSocket serverSocket = new ServerSocket(port);
        while (counter < limit) {
            counter++;
            System.out.println("Waiting for client");
            Socket client = serverSocket.accept();

            PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
            //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            dispatcher.addClientToMap(client.getInetAddress().getHostAddress(), client);

            ClientHandler clientHandler = new ClientHandler(client, activeClients,dispatcher);
            clientHandler.start();


        }
    }


}
