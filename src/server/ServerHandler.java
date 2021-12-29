package server;

import message.Message;
import message.MyInputStream;
import message.MyOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    Socket client;
    Server server;
    int index;
    MyInputStream ois;
    MyOutputStream oos;
    ServerUI w;

    ServerHandler(Socket c,Server server,int index,ServerUI w){
        client = c;this.server = server;this.index = index;this.w = w;

        String content = "客户机"+client.getInetAddress().getHostAddress()+"加入游戏间\n";
        w.context.append(content);

        if (index%2==0) {//如果当前有新的一对客户端进入等待状态，就对他们进行黑白子划分
            Socket s1 = server.clientSockets.get(index - 2);
            Socket s2 = server.clientSockets.get(index - 1);
            int x = (int) Math.random() * 137;
            int p1 = x % 2;
            int p2 = (x % 2) ^ 1;
            Message msg1 = new Message(0, 0, 0, "" + p1);
            Message msg2 = new Message(0, 0, 0, "" + p2);
            try {
                oos = new MyOutputStream(s1.getOutputStream());
                oos.writeObject(msg1);
                oos = new MyOutputStream(s2.getOutputStream());
                oos.writeObject(msg2);
                server.server_ui.context.append(s1.getInetAddress().getHostAddress()+
                        "执"+(p1==0 ? "黑子\n" : "白子\n"));
                server.server_ui.context.append(s2.getInetAddress().getHostAddress()+
                        "执"+(p2==0 ? "黑子\n" : "白子\n"));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    public void run() {
        String content;
        while (true) {
            try {
                ois = new MyInputStream(client.getInputStream());

                try {
                    Message msg = (Message) ois.readObject();
                    if (index % 2 == 0) {
                        oos = new MyOutputStream(server.clientSockets.get(index - 2).getOutputStream());
                        oos.writeObject(msg);
                    } else {
                        oos = new MyOutputStream(server.clientSockets.get(index).getOutputStream());
                        oos.writeObject(msg);
                    }
                    content = client.getInetAddress().getHostAddress() + "put:"
                            + msg.getContext() + "\n";
                    w.context.append(content);

                } catch (ClassNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
