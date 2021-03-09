package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {


    Socket client;
    PrintWriter pw;
    BufferedReader br;
    boolean keepRunning = true;

    public void connect(String ipAdress, int port) throws IOException {
        client = new Socket(ipAdress, port);

        //Den måde clienten skriver til serveren
        pw = new PrintWriter(client.getOutputStream(),true);

        //Den måde clienten modtager input fra serveren
        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println(br.readLine());

        Scanner keyboard = new Scanner(System.in);
        while(keepRunning) {
            String msgToSend = keyboard.nextLine();
            pw.println(msgToSend);

            //Modtager og printer msgToSend som nu er blevet upperCased, lowercased eller noget andet
            System.out.println(br.readLine());
            if (msgToSend.equals("stop")) {
                keepRunning = false;
            }
        }
        client.close();
    }
    public static void main(String[] args) throws IOException {
        new ChatClient().connect("localhost", 8088);
    }
}
