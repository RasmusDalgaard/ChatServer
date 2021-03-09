package client;

import server.Dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler extends Thread {
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    Socket client;
    String clientName;

    BlockingQueue<String> allMessages;

    public ClientHandler(Socket client, String clientName, BlockingQueue<String> allMessages) {
        this.client = client;
        this.clientName = clientName;
        this.allMessages = allMessages;
        try {
            this.printWriter = new PrintWriter(client.getOutputStream(), true);
            this.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            protocol();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void protocol() throws IOException {
        //TODO: SEND#Hans#Hello Hans (Peter implicit afsender)     SEND#Peter,Hans#Hello hans       //TODO: MESSAGE#Peter#Hello Hans
        String command = bufferedReader.readLine();
        String[] commandArray = command.split("#");
        String token = commandArray[0];
        String message = token + "#" + commandArray[1] + "#" + commandArray[2];

        while (true) {
            switch (token) {
                //case "CONNECT": Method; break;
                case "SEND": handleSend(message);
                case "commando3": //Method; break;
                case "commando4": //Method; break;
                case "commando5" : //Method; break;
            }
        }
    }

    public void handleSend(String message) {
        String inputToDispatcher = message;
        allMessages.add(inputToDispatcher);

    }



}
