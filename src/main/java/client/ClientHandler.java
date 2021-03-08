package client;

import com.sun.security.ntlm.Client;
import server.Dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentMap;

public class ClientHandler extends Thread {
    PrintWriter printWriter;
    BufferedReader bufferedReader;
    Socket client;
    Dispatcher dispatcher;
    ConcurrentMap<String, Socket> activeclients;

    public ClientHandler(Socket client, ConcurrentMap<String, Socket> activeclients, Dispatcher dispatcher) {
        this.client = client;
        this.dispatcher = dispatcher;
        this.activeclients = activeclients;
        try {
            this.printWriter = new PrintWriter(client.getOutputStream(), true);
            this.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    public void protocol() throws IOException {
        String command = bufferedReader.readLine();
        while (true) {
            switch (command) {
                case "CONNECT": //Method; break;
                case "commando2": //Method; break;
                case "commando3": //Method; break;
                case "commando4": //Method; break;
                case "commando5" : //Method; break;
            }
        }
    }

    public void handleConnect() {
        activeclients.put(client.getInetAddress().getHostAddress(),client);
        if (activeclients.containsKey(client.getInetAddress())) {
            printWriter.println("CONNECT#");
        }
    }

}
