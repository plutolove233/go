package message;

import java.io.IOException;
import java.net.Socket;

public class Contract {
    Contract(){}
    public static void sendMessage(Socket sc, Message msg){
        MyOutputStream oos;
        try {
            oos = new MyOutputStream(sc.getOutputStream());
            oos.writeObject(msg);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Message getMessage(Socket sc){
        MyInputStream ois;
        try {
            ois = new MyInputStream(sc.getInputStream());
            Message msg = (Message)ois.readObject();
            return msg;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
