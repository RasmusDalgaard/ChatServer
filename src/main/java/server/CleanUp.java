package server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class CleanUp extends Thread{
    BlockingQueue<String> clientsToClose;
    ConcurrentMap<String, Socket> activeClients;

    public CleanUp (BlockingQueue<String> clientsToClose,ConcurrentMap<String, Socket> activeClients ) {
        this.clientsToClose = clientsToClose;
        this.activeClients = activeClients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String clientName = clientsToClose.take();
                closeClient(clientName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void closeClient(String clientName) {
        try {
            activeClients.get(clientName).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
