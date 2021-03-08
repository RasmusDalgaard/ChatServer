package server;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.PrimitiveIterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

public class Dispatcher extends Thread {

    ConcurrentMap<String, Socket> activeClients;
    BlockingQueue<PrintWriter> allMessages;

    public Dispatcher(ConcurrentMap activeClients) {
        this.activeClients = activeClients;
        allMessages = new ArrayBlockingQueue<>(10);
    }

    @Override
    public void run() {

    }

    public void addWriterToList(PrintWriter printWriter) {
        allMessages.add(printWriter);
    }

    public void addClientToMap(String ipAdress, Socket client) {
        activeClients.put(ipAdress, client);
    }
}
