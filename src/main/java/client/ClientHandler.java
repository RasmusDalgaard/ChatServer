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
        //  TODO:  SEND#Hans#Hello hans
        String command = bufferedReader.readLine();
        String[] commandArray = command.split("#");
        String token = commandArray[0];
        //  TODO:  SEND#Peter,Hans#Hello hans
        String message = token + "#" + clientName + "," + commandArray[1] + "#" + commandArray[2];
            while (true) {
                switch (token) {
                    //case "CONNECT": Method; break;
                    case "SEND":
                        handleSend(message);
                    case "commando3": //Method; break;
                    case "commando4": //Method; break;
                    case "commando5": //Method; break;
                }
            }
    }

    public void handleSend(String message) {
        //TODO: SEND#Peter,Hans#Hello hans
        String inputToDispatcher = message;
        allMessages.add(inputToDispatcher);

    }



}
