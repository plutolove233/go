package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    public Vector<Socket> clientSockets = new Vector<>();
    public ServerSocket serverSocket;
    public ServerUI server_ui;
    void service() {
        server_ui = new ServerUI();
        int i = 0;
        try {
            serverSocket = new ServerSocket(8000);
            while (true) {
                Socket sc = serverSocket.accept();
                clientSockets.addElement(sc);
                i++;
                Thread thread = new Thread(new ServerHandler(sc,this,i,server_ui));
                thread.start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        new Server().service();
    }
}
