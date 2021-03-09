package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientHandlerTest {

    ClientHandler cl;
    BlockingQueue<String> allMessages;
    Thread testThread;

    @BeforeEach
    void setUp() {

        String name = "Kurt";
        allMessages = new ArrayBlockingQueue<>(250);
        String userInput = "SEND#LONE#Hej fra Kurt";

        PrintWriter pw = new PrintWriter(System.out);
        BufferedReader br = new BufferedReader(new StringReader(userInput));

        cl = new ClientHandler(name, br, pw, allMessages);

        testThread = new Thread(cl);
    }

    @Test
    void testClientSendMsg() throws InterruptedException {
        testThread.start();
        testThread.join();

        System.out.println(allMessages);

        int expected = 1;
        int actual = allMessages.size();

        assertEquals(expected, actual);
    }
}