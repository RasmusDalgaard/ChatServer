package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Writer extends Thread {
    PrintWriter pw;
    Scanner sc;
    public Writer(PrintWriter pw) {
        this.pw = pw;
        sc = new Scanner(System.in);
    }

    @Override
    public void run() {
        String output = "";
        boolean running = true;
        while (running) {
            output = sc.nextLine();
            pw.println(output);
        }
    }
}

class Reader extends Thread {
    BufferedReader br;

    public Reader(BufferedReader br) {
        this.br = br;
    }

    @Override
    public void run() {
        String input = "";
        boolean running = true;
        while (running) {
            try {
                input = br.readLine();
                if (input.contains("CLOSE")) {
                    System.out.println(input);
                    running = false;
                } else {
                    System.out.println(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ChatClient {


    Socket client;
    Writer writer;
    Reader reader;

    PrintWriter pw;
    BufferedReader br;
    boolean keepRunning = true;


    public void connect(String ipAdress, int port) throws IOException {
        client = new Socket(ipAdress, port);

        //Den måde clienten skriver til serveren
        pw = new PrintWriter(client.getOutputStream(),true);
        writer = new Writer(pw);
        writer.start();

        //Den måde clienten modtager input fra serveren
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        reader = new Reader(br);
        reader.start();
    }
    public static void main(String[] args) throws IOException {
        new ChatClient().connect("localhost", 8088);
    }
}
