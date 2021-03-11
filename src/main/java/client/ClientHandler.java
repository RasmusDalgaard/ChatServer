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

    public ClientHandler(String name, BufferedReader br, PrintWriter pw, BlockingQueue<String> allMessages) {
        this.clientName = name;
        this.bufferedReader = br;
        this.printWriter = pw;
        this.allMessages = allMessages;
    }

    @Override
    public void run() {
        try {
            printWriter.println("");
            protocol();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void protocol() throws IOException {
        boolean running = true;
        while (running) {
            //TODO:  SEND#Hans#Hello hans
            String clientInput = bufferedReader.readLine();
            if (clientInput == null) {
                clientInput = "CLOSE#";
            }

            String[] clientInputArray = clientInput.split("#");
            //NO input exception
            String token = clientInputArray[0];
            switch (token) {
                //case "CONNECT": Method; break;
                case "SEND":
                    String message = token + "#" + clientName + "," + clientInputArray[1] + "#" + clientInputArray[2];
                    handleSend(message);
                    break;
                case "CLOSE":
                    running = false;
                    handleClose("0");
                    break;
                default:
                    running = false;
                    handleClose("1");
            }
        }
    }

    public void handleSend(String message) {
        //SEND#clientName,receiver#MessageToReceiver
        String inputToDispatcher = message;
        allMessages.add(inputToDispatcher);
    }


    public void handleClose(String closeMessage) {
        String inputToDispatcher = "CLOSE#" +  clientName + "#" + closeMessage;
        allMessages.add(inputToDispatcher);
    }

}
