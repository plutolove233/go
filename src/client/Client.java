package client;

import go.Chess;
import message.Message;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        Chess board = new Chess();
        try {
            Socket host = new Socket("127.0.0.1",8000);
            ClientUI w = new ClientUI(board,host);
            w.Run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
